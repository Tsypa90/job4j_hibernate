package ru.job4j.hibernate.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            VacancyBase vacancyBase = new VacancyBase();
            Vacancy first = Vacancy.of("Java Junior");
            Vacancy second = Vacancy.of("Java Team Lead");
            vacancyBase.vacancyAdd(first);
            vacancyBase.vacancyAdd(second);
            Candidate candidate = Candidate.of("Pavel", "Java", 1000d);
            candidate.setVacancyBase(vacancyBase);

            session.save(candidate);

            var list = session.createQuery(
                    "select distinct c from Candidate c "
                    + "join fetch c.vacancyBase vb "
                    + "join fetch vb.vacancyList vl "
                    + "where c.id = :cId", Candidate.class).setParameter("cId", 1).uniqueResult();
            System.out.println(list);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
