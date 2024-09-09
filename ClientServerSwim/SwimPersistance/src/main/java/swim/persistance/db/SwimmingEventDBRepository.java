package swim.persistance.db;

import swim.model.SwimmingEvent;
import swim.model.enums.Distance;
import swim.model.enums.Style;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swim.persistance.db.JdbcUtils;
import swim.persistance.interfaces.SwimmingEventRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SwimmingEventDBRepository implements SwimmingEventRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public SwimmingEventDBRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(SwimmingEvent elem) {
        logger.traceEntry("saving SwimmingEvent {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into SwimmingEvents (distance, style) values (?,?)")) {
            preStmt.setString(1, elem.getDistance().toString());
            preStmt.setString(2, elem.getStyle().toString());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(SwimmingEvent elem) {
        logger.traceEntry("deleting SwimmingEvent {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from SwimmingEvents where id=?")) {
            preStmt.setInt(1, elem.getID());
            int result = preStmt.executeUpdate();
            logger.trace("Deleting {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(SwimmingEvent elem, Integer id) {
        logger.traceEntry("updating SwimmingEvent {} where id {}", elem, id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update SwimmingEvents set distance=?, style=? where id=?")) {
            preStmt.setString(1, elem.getDistance().toString());
            preStmt.setString(2, elem.getStyle().toString());
            preStmt.setInt(3, id);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public SwimmingEvent findById(Integer id) {
        logger.traceEntry("finding the SwimmingEvent with the id {}", id);
        Connection con = dbUtils.getConnection();
        SwimmingEvent SwimmingEvent = null;

        try (PreparedStatement preStmt = con.prepareStatement("select * from SwimmingEvents where id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String distance = result.getString("distance");
                    String style = result.getString("style");
                    SwimmingEvent = new SwimmingEvent(Distance.valueOf(distance), Style.valueOf(style));
                    SwimmingEvent.setID(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(SwimmingEvent);
        return SwimmingEvent;
    }

    @Override
    public Iterable<SwimmingEvent> findAll() {
        System.out.println("Am ajuns in repo");
        logger.traceEntry("finding all SwimmingEvents");
        Connection con = dbUtils.getConnection();
        List<SwimmingEvent> SwimmingEventList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from SwimmingEvents")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String distance = result.getString("distance");
                    String style = result.getString("style");
                    SwimmingEvent SwimmingEvent = new SwimmingEvent(Distance.valueOf(distance), Style.valueOf(style));
                    SwimmingEvent.setID(id);
                    SwimmingEventList.add(SwimmingEvent);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(SwimmingEventList);
        return SwimmingEventList;
    }

}
