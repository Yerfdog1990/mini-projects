package util;

import model.StudentEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.function.Function;

public class HibernateUtil {
    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        try{
            if(sessionFactory == null){
                sessionFactory = new Configuration().configure(HIBERNATE_CFG_XML).buildSessionFactory();
            }
            return sessionFactory;
        } catch (Exception e){
            System.err.println("Failed to create sessionFactory object." + e);
            throw new ExceptionInInitializerError(e);
        }
    }
    public static Session getSession(){
        return getSessionFactory().openSession();
    }

    public static <T> T doWithSession(Function<Session, T> rollBaCK){
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try{
            T result = rollBaCK.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e){
            transaction.rollback();
            System.err.println("Transaction failed: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

}
