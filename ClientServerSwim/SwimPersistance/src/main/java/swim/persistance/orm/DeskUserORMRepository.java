package swim.persistance.orm;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import swim.model.hbm.DeskUser;
import swim.persistance.interfaces.DeskUserRepository;

import java.util.List;

public class DeskUserORMRepository implements DeskUserRepository {
    private static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public DeskUserORMRepository() {
        initialize();
    }

    @Override
    public DeskUser findByEmail(String emaill) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query query = session.createQuery("from DeskUser D where D.email = ?");
                query.setParameter(0, emaill);
                List<DeskUser> user = query.list();
                tx.commit();
                return user.get(0);
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    @Override
    public void add(DeskUser elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                int id = (int) session.save(elem);
                elem.setID(id);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(DeskUser elem) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                DeskUser user = session.load(DeskUser.class, elem.getID());
                session.delete(user);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la stergere " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(DeskUser elem, Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                DeskUser user = session.load(DeskUser.class, id);
                elem.setID(id);
                session.update(elem);
                tx.commit();

            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public DeskUser findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                DeskUser user = session.get(DeskUser.class, id);
                tx.commit();
                return user;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    @Override
    public Iterable<DeskUser> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<DeskUser> useri =
                        session.createQuery("from DeskUser as u", DeskUser.class).
                                list();
                tx.commit();
                return useri;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }
}
