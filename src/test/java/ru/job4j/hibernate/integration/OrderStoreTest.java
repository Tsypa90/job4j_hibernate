package ru.job4j.hibernate.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
public class OrderStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = pool.getConnection().prepareStatement("drop table orders")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrderStore store = new OrderStore(pool);

        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveThreeOrdersAndFindAll() {
        OrderStore store = new OrderStore(pool);
        List<Order> orders = List.of(Order.of("first", "first"),
                Order.of("second", "second"), Order.of("third", "third"));

        orders.forEach(store::save);

        List<Order> result = (List<Order>) store.findAll();

        assertThat(result, is(orders));
    }

    @Test
    public void whenSaveOrderThenUpdateAndTrue() {
        OrderStore store = new OrderStore(pool);
        Order order = Order.of("first", "first");
        store.save(order);
        order.setName("second");
        order.setDescription("second");
        boolean rsl = store.update(order);
        assertTrue(rsl);
        assertThat(store.findById(order.getId()).getName(), is("second"));
    }

    @Test
    public void whenUpdateAndFalse() {
        OrderStore store = new OrderStore(pool);
        Order order = Order.of("first", "first");
         var rsl = store.update(order);
         assertFalse(rsl);
    }

    @Test
    public void whenSaveTwoWithSameNamesAndGetTwo() {
        OrderStore store = new OrderStore(pool);
        List<Order> orders = List.of(Order.of("first", "first"),
                Order.of("first", "first"));
        orders.forEach(store::save);
        var rsl = store.findByName("first");
        assertThat(rsl, is(orders));
        assertThat(rsl.get(0).getName(), is("first"));
    }
}