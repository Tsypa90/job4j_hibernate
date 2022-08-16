package ru.job4j.hibernate.integration;

import java.sql.Timestamp;
import java.util.Objects;

public class Order {
    private int id;
    private String name;
    private String descrip;
    private Timestamp created;

    public Order() {
    }

    public Order(int id, String name, String description, Timestamp created) {
        this.id = id;
        this.name = name;
        this.descrip = description;
        this.created = created;
    }

    public static Order of(String name, String description) {
        Order order = new Order();
        order.name = name;
        order.descrip = description;
        order.created = new Timestamp(System.currentTimeMillis());
        return order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return descrip;
    }

    public void setDescription(String description) {
        this.descrip = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
