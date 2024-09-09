package swim.persistance.db;


import swim.model.Contestant;
import swim.model.Registration;
import swim.model.enums.Distance;
import swim.model.enums.Style;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swim.persistance.db.JdbcUtils;
import swim.model.SwimmingEvent;
import swim.model.Tuple;
import swim.persistance.interfaces.RegistrationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RegistrationDBRepository implements RegistrationRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RegistrationDBRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Registration elem) {
        logger.traceEntry("saving Registration {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Registrations (id_contestant, id_event) values (?,?)")) {
            preStmt.setInt(1, elem.getContestant().getID());
            preStmt.setInt(2, elem.getEvent().getID());
            System.out.println(elem.getContestant().getID() + "   " + elem.getEvent().getID());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Registration elem) {
        logger.traceEntry("deleting Registration {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Registrations where id_contestant=? and id_event=?")) {
            preStmt.setInt(1, elem.getContestant().getID());
            preStmt.setInt(2, elem.getEvent().getID());
            int result = preStmt.executeUpdate();
            logger.trace("Deleting {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Registration elem, Tuple<Contestant, SwimmingEvent> id) {
    }

    @Override
    public Registration findById(Tuple<Contestant, SwimmingEvent> id) {
        logger.traceEntry("finding the Registration with the id {}", id);
        Connection con = dbUtils.getConnection();
        Registration registration = null;

        try (PreparedStatement preStmt = con.prepareStatement("select * from Contestants C inner join Registrations R on C.id=R.id_contestant inner join SwimmingEvents SE on SE.id=R.id_event where R.id_contestant=? and R.id_event=?")) {
            preStmt.setInt(1, id.getLeftMember().getID());
            preStmt.setInt(2, id.getRightMember().getID());
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id_contestant = result.getInt("id_contestant");
                    String name_contestant = result.getString("name");
                    int age_contestant = result.getInt("age");
                    Contestant contestant = new Contestant(name_contestant, age_contestant);
                    contestant.setID(id_contestant);

                    int id_event = result.getInt("id_event");
                    String distance = result.getString("distance");
                    String style = result.getString("style");
                    SwimmingEvent event = new SwimmingEvent(Distance.valueOf(distance), Style.valueOf(style));
                    event.setID(id_event);

                    registration = new Registration(contestant, event);

                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(registration);
        return registration;
    }

    @Override
    public Iterable<Registration> findAll() {
        logger.traceEntry("finding all Registrations");
        Connection con = dbUtils.getConnection();
        List<Registration> RegistrationList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Contestants C inner join Registrations R on C.id=R.id_contestant inner join SwimmingEvents SE on SE.id=R.id_event")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id_contestant = result.getInt("id_contestant");
                    String name_contestant = result.getString("name");
                    int age_contestant = result.getInt("age");
                    Contestant contestant = new Contestant(name_contestant, age_contestant);
                    contestant.setID(id_contestant);

                    int id_event = result.getInt("id_event");
                    String distance = result.getString("distance");
                    String style = result.getString("style");
                    SwimmingEvent event = new SwimmingEvent(Distance.valueOf(distance), Style.valueOf(style));
                    event.setID(id_event);

                    Registration registration = new Registration(contestant, event);
                    RegistrationList.add(registration);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(RegistrationList);
        return RegistrationList;
    }

    @Override
    public Integer nrOfContestants(SwimmingEvent swimmingEvent) {
        logger.traceEntry("counting all the contestants of an event");
        Connection con = dbUtils.getConnection();

        int nr=-1;
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as nr from Registrations R where id_event=?")) {
            preStmt.setInt(1, swimmingEvent.getID());
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    nr=result.getInt("nr");
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(nr);
        return nr;
    }

    @Override
    public List<Contestant> getSubmittedContestants(SwimmingEvent swimmingEvent) {
        logger.traceEntry("get all submitted contestants of an swimming event");
        Connection con = dbUtils.getConnection();
        List<Contestant> submittedContestants = new ArrayList<>();

        try (PreparedStatement preStmt = con.prepareStatement("select * from Registrations R inner join Contestants C on C.id = R.id_contestant where id_event=?")) {
            preStmt.setInt(1, swimmingEvent.getID());
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id_contestant = result.getInt("id_contestant");
                    String name_contestant = result.getString("name");
                    int age_contestant = result.getInt("age");
                    Contestant contestant = new Contestant(name_contestant, age_contestant);
                    contestant.setID(id_contestant);
                    submittedContestants.add(contestant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(submittedContestants);
        return submittedContestants;
    }

    @Override
    public List<SwimmingEvent> getRegisteredEvents(Contestant contestant) {
        logger.traceEntry("finding all registered swimming events of a contestant");
        Connection con = dbUtils.getConnection();
        List<SwimmingEvent> RegistrationList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Registrations R inner join SwimmingEvents SE on SE.id = R.id_event where id_contestant=?")) {
            preStmt.setInt(1, contestant.getID());
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id_event = result.getInt("id_event");
                    String distance = result.getString("distance");
                    String style = result.getString("style");
                    SwimmingEvent event = new SwimmingEvent(Distance.valueOf(distance), Style.valueOf(style));
                    event.setID(id_event);

                    RegistrationList.add(event);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(RegistrationList);
        return RegistrationList;
    }
}



