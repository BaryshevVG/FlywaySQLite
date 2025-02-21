package com.example;

import org.junit.jupiter.api.Test;
import org.hibernate.query.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest extends AbstractTest {

    private Customer getAnyCustomer() {
        Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
        List<Customer> customers = query.getResultList();
        assertFalse(customers.isEmpty(), "В базе данных нет клиентов!");
        return customers.get(0); // Берём первого попавшегося
    }

    @Test
    public void testCreateOrder() {
        Customer customer = getAnyCustomer();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(java.time.LocalDateTime.now());

        session.save(order);
        session.flush();

        assertNotNull(order.getOrderId());
    }

    @Test
    public void testReadOrder() {
        Customer customer = getAnyCustomer();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(java.time.LocalDateTime.now());
        session.save(order);
        session.flush();

        Order fetchedOrder = session.get(Order.class, order.getOrderId());
        assertNotNull(fetchedOrder);
        assertEquals(customer.getId(), fetchedOrder.getCustomer().getId());
    }

    @Test
    public void testUpdateOrder() {
        Customer customer = getAnyCustomer();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(java.time.LocalDateTime.now());
        session.save(order);
        session.flush();

        order.setDateGet(java.time.LocalDateTime.of(2023, 1, 1, 12, 0));
        session.update(order);
        session.flush();

        Order updatedOrder = session.get(Order.class, order.getOrderId());
        assertEquals(java.time.LocalDateTime.of(2023, 1, 1, 12, 0), updatedOrder.getDateGet());
    }

    @Test
    public void testDeleteOrder() {
        Customer customer = getAnyCustomer();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(java.time.LocalDateTime.now());
        session.save(order);
        session.flush();

        session.delete(order);
        session.flush();

        Order deletedOrder = session.get(Order.class, order.getOrderId());
        assertNull(deletedOrder);
    }
}
