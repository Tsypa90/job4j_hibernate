package ru.job4j.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.stream.IntStream;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();

        try {
            SessionFactory sf = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Candidate one = Candidate.of("Pavel", "java", 3500);
            Candidate two = Candidate.of("Pavel", "java", 3500);
            Candidate three = Candidate.of("Andrey", "python", 3400);
            session.save(one);
            session.save(two);
            session.save(three);
            session.createQuery("from Candidate").list().forEach(System.out::println);
            session.createQuery("update Candidate c set c.name = :fName, c.experience = :fExp, c.salary = :fSalary where c.id = :fId")
                    .setParameter("fName", "Igor")
                    .setParameter("fExp", "C#")
                    .setParameter("fSalary", 2800.00)
                    .setParameter("fId", 2)
                    .executeUpdate();
            Query query = session.createQuery("from Candidate c where c.id = :fId");
            query.setParameter("fId", 2);
            System.out.println(query.uniqueResult());
            session.createQuery("from Candidate c where c.name = :fName")
                    .setParameter("fName", "Pavel")
                            .list().forEach(System.out::println);
            IntStream.range(1, 4).forEach(s -> session.createQuery("delete Candidate c where c.id = :fId")
                        .setParameter("fId", s).executeUpdate());
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
