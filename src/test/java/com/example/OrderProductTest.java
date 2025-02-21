package com.example;

import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderProductTest extends AbstractTest {
    private Customer getAnyCustomer() {
        Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
        List<Customer> customers = query.getResultList();
        assertFalse(customers.isEmpty(), "В базе данных нет клиентов!");
        return customers.get(0); // Берём первого попавшегося
    }
    @Test
    public void testCreateOrderProduct() {
        Customer customer = getAnyCustomer();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(java.time.LocalDateTime.now());
        session.save(order);
        session.flush();

        Product product = new Product();
        product.setMenuName("Shaurma");
        product.setPrice(10.99);

        session.persist(product);
        session.flush();

        assertNotNull(product.getId());

        OrderProduct orderProduct = new OrderProduct(order, product, 2);
        session.save(orderProduct);
        session.flush();

        assertNotNull(orderProduct.getId());
    }

    @Test
    public void testReadOrderProduct() {
        Customer customer = getAnyCustomer();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(java.time.LocalDateTime.now());
        session.save(order);
        session.flush();

        Product product = new Product();
        product.setMenuName("Shaurma");
        product.setPrice(15.99);

        session.persist(product);
        session.flush();

        assertNotNull(product.getId());

        OrderProduct orderProduct = new OrderProduct(order, product, 3);
        session.save(orderProduct);
        session.flush();

        OrderProduct fetchedOrderProduct = session.get(OrderProduct.class, orderProduct.getId());
        assertNotNull(fetchedOrderProduct);
        assertEquals(3, fetchedOrderProduct.getQuantity());
    }

    @Test
    public void testUpdateOrderProduct() {
        Customer customer = getAnyCustomer();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(java.time.LocalDateTime.now());
        session.save(order);
        session.flush();

        Product product = new Product();
        product.setMenuName("№3");
        product.setPrice(5.99);

        session.persist(product);
        session.flush();

        assertNotNull(product.getId());

        OrderProduct orderProduct = new OrderProduct(order, product, 4);
        session.save(orderProduct);
        session.flush();

        orderProduct.setQuantity(10);
        session.update(orderProduct);
        session.flush();

        OrderProduct updatedOrderProduct = session.get(OrderProduct.class, orderProduct.getId());
        assertEquals(10, updatedOrderProduct.getQuantity());
    }

    @Test
    public void testDeleteOrderProduct() {
        Customer customer = getAnyCustomer();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(java.time.LocalDateTime.now());
        session.save(order);
        session.flush();

        Product product = new Product();
        product.setMenuName("№4");
        product.setPrice(10.00);
        session.persist(product);
        session.flush();

        assertNotNull(product.getId());

        OrderProduct orderProduct = new OrderProduct(order, product, 1);
        session.save(orderProduct);
        session.flush();

        session.createQuery("DELETE FROM OrderProduct WHERE order.id = :orderId AND product.id = :productId")
                .setParameter("orderId", orderProduct.getOrder().getOrderId())
                .setParameter("productId", orderProduct.getProduct().getId())
                .executeUpdate();

        session.flush();
        session.clear();

        OrderProduct foundOrderProduct = session.createQuery(
                        "FROM OrderProduct WHERE order.id = :orderId AND product.id = :productId", OrderProduct.class)
                .setParameter("orderId", orderProduct.getOrder().getOrderId())
                .setParameter("productId", orderProduct.getProduct().getId())
                .uniqueResult();

        assertNull(foundOrderProduct);

    }
}
