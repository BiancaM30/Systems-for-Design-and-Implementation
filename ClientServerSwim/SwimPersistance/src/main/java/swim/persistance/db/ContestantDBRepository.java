package swim.persistance.db;

import swim.model.Contestant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swim.persistance.interfaces.ContestantRepository;
import swim.persistance.db.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ContestantDBRepository implements ContestantRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ContestantDBRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Contestant elem) {
        logger.traceEntry("saving contestant {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Contestants (name, age) values (?,?)")) {
            preStmt.setString(1, elem.getName());
            preStmt.setInt(2, elem.getAge());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Contestant elem) {
        logger.traceEntry("deleting contestant {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Contestants where id=?")) {
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
    public void update(Contestant elem, Integer id) {
        logger.traceEntry("updating contestant {} where id {}", elem, id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Contestants set name=?, age=? where id=?")) {
            preStmt.setString(1, elem.getName());
            preStmt.setInt(2, elem.getAge());
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
    public Contestant findById(Integer id) {
        logger.traceEntry("finding the contestant with the id {}", id);
        Connection con = dbUtils.getConnection();
        Contestant contestant = null;

        try (PreparedStatement preStmt = con.prepareStatement("select * from Contestants where id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String name = result.getString("name");
                    int age = result.getInt("age");
                    contestant = new Contestant(name, age);
                    contestant.setID(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(contestant);
        return contestant;
    }

    @Override
    public Iterable<Contestant> findAll() {

        logger.traceEntry("finding all contestants");
        Connection con = dbUtils.getConnection();
        List<Contestant> contestantList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Contestants")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    int age = result.getInt("age");
                    Contestant contestant = new Contestant(name, age);
                    contestant.setID(id);
                    contestantList.add(contestant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(contestantList);
        return contestantList;
    }

}
