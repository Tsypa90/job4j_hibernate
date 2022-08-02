package ru.job4j.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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

            Model one = Model.of("Prius");
            Model two = Model.of("Camry");
            Model three = Model.of("Hiace");
            Model four = Model.of("Allion");
            Model five = Model.of("Land Cruiser");
            session.save(one);
            session.save(two);
            session.save(three);
            session.save(four);
            session.save(five);

            Brand brand = Brand.of("Toyota");

            brand.addModel(one);
            brand.addModel(two);
            brand.addModel(three);
            brand.addModel(four);
            brand.addModel(five);

            session.save(brand);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
