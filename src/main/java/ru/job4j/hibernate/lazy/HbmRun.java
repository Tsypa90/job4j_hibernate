package ru.job4j.hibernate.lazy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        List<Brand> brands = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Brand one = Brand.of("Toyota");
            Model first = Model.of("Prius", one);
            Model second  = Model.of("TLC", one);
            Model third = Model.of("Camry", one);
            one.getModels().add(first);
            one.getModels().add(second);
            one.getModels().add(third);
            session.persist(one);
            brands = session.createQuery("select distinct c from Brand c join fetch c.models").list();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Model model : brands.get(0).getModels()) {
            System.out.println(model);
        }
    }
}
