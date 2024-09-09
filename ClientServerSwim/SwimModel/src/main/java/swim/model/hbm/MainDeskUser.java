package swim.model.hbm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Date;
import java.util.List;

public class MainDeskUser {
    private static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public static void main(String ... arg) {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new DeskUser("ioana@yahoo.com", "ioana"));
            session.save(new DeskUser("maria@yahoo.com", "maria"));
            session.getTransaction().commit();
        }


        try(Session  session = sessionFactory.openSession()){
            session.beginTransaction();
            List<DeskUser> result = session.createQuery("from DeskUser", DeskUser.class).list();
            for (DeskUser event :  result) {
                System.out.println("Event (" + event.getEmail() + ") : " + event.getPassword());
            }
            session.getTransaction().commit();
        }

        close();
    }
}
