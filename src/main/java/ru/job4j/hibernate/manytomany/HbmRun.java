package ru.job4j.hibernate.manytomany;

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
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Author one = Author.of("Pushkin");
            Author two = Author.of("Dostoevsky");
            Author three = Author.of("Dal");

            Book first = Book.of("Story");
            first.getAuthors().add(one);
            first.getAuthors().add(three);

            Book second = Book.of("Bess");
            second.getAuthors().add(two);

            session.persist(first);
            session.persist(second);

            Book book = session.get(Book.class, 1);
            session.remove(book);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
