package swim.persistance.orm;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class InitUtils {

    private static SessionFactory instance = null;

    public InitUtils() {
    }

    private SessionFactory getNewConnection() {
        SessionFactory con = null;

        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            con = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return con;
    }

    public SessionFactory getConnection() {
        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();

        } catch (Exception e) {
            System.out.println("Error" + e);
        }
        return instance;
    }
}
