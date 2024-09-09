package swim.persistance.db;

import swim.model.hbm.DeskUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swim.persistance.interfaces.DeskUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DeskUserDBRepository implements DeskUserRepository {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public DeskUserDBRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }


    @Override
    public void add(DeskUser elem) {
        logger.traceEntry("saving DeskUser {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into DeskUsers (email, password) values (?,?)")) {
            preStmt.setString(1, elem.getEmail());
            preStmt.setString(2, elem.getPassword());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(DeskUser elem) {
        logger.traceEntry("deleting DeskUser {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from DeskUsers where id=?")) {
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
    public void update(DeskUser elem, Integer id) {
        logger.traceEntry("updating DeskUser {} where id {}", elem, id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update DeskUsers set email=?, password=? where id=?")) {
            preStmt.setString(1, elem.getEmail());
            preStmt.setString(2, elem.getPassword());
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
    public DeskUser findById(Integer id) {
        logger.traceEntry("finding the DeskUser with the id {}", id);
        Connection con = dbUtils.getConnection();
        DeskUser DeskUser = null;

        try (PreparedStatement preStmt = con.prepareStatement("select * from DeskUsers where id=?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String email = result.getString("email");
                    String password = result.getString("password");
                    DeskUser = new DeskUser(email, password);
                    DeskUser.setID(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(DeskUser);
        return DeskUser;
    }

    @Override
    public Iterable<DeskUser> findAll() {

        logger.traceEntry("finding all DeskUsers");
        System.out.println("in repooooooooooooooooo");
        Connection con = dbUtils.getConnection();
        List<DeskUser> DeskUserList = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from DeskUsers")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    DeskUser DeskUser = new DeskUser(email, password);
                    DeskUser.setID(id);
                    DeskUserList.add(DeskUser);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(DeskUserList);
        return DeskUserList;
    }


    @Override
    public DeskUser findByEmail(String email) {
        logger.traceEntry("finding the DeskUser with the email {}", email);
        Connection con = dbUtils.getConnection();
        DeskUser DeskUser = null;

        try (PreparedStatement preStmt = con.prepareStatement("select * from DeskUsers where email=?")) {
            preStmt.setString(1, email);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String password = result.getString("password");
                    DeskUser = new DeskUser(email, password);
                    DeskUser.setID(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(DeskUser);
        return DeskUser;
    }
}
