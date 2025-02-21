package com.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class DeliveryTest extends AbstractTest {

    private void safeFlush() {
        int maxRetries = 5;
        long delayMs = 1000;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                if (!session.getTransaction().isActive()) {
                    session.beginTransaction();
                }
                session.getTransaction().commit();
                session.beginTransaction();
                return;
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }

                if (isTransientDatabaseError(e)) {
                    attempt++;
                    System.err.println("Попытка " + attempt + ": database is locked. Ждем " + delayMs + " мс...");
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ignored) {}

                    if (attempt == maxRetries) {
                        session.close();
                        session = sessionFactory.openSession();
                        session.beginTransaction();
                    }
                } else {
                    throw new RuntimeException("Ошибка при safeCommit", e);
                }
            }
        }
        throw new RuntimeException("safeCommit не удалось выполнить после " + maxRetries + " попыток");
    }

    private boolean isTransientDatabaseError(Exception e) {
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause.getMessage() != null && cause.getMessage().contains("database is locked")) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private Order createNewOrder() {
        Customer customer = session.createQuery("FROM Customer", Customer.class)
                .getResultList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Нет доступных клиентов в базе данных."));
        Order order = new Order();
        order.setCustomer(customer);
        order.setDateGet(LocalDateTime.now());
        session.save(order);
        safeFlush();
        return order;
    }

    private Courier getAnyCourier() {
        List<Courier> couriers = session.createQuery("FROM Courier", Courier.class).getResultList();
        assertFalse(couriers.isEmpty(), "Нет доступных курьеров в базе данных.");
        return couriers.get(0);
    }

    @Test
    public void testCreateDelivery() {
        Order order = createNewOrder();
        Courier courier = getAnyCourier();

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setCourier(courier);
        delivery.setDateArrived(LocalDateTime.now());
        delivery.setTaken("Yes");
        delivery.setPaymentMethod("Cash");

        session.save(delivery);
        safeFlush();

        assertNotNull(delivery.getDeliveryId());

        if (session.contains(delivery)) {
            session.delete(delivery);
            safeFlush();
        }
    }

    @Test
    public void testReadDelivery() {
        Courier courier = getAnyCourier();
        Order order = createNewOrder();

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setCourier(courier);
        delivery.setDateArrived(LocalDateTime.now());
        delivery.setTaken("No");
        delivery.setPaymentMethod("Card");

        session.save(delivery);
        session.flush();

        assertNotNull(delivery.getDeliveryId());

        Delivery fetchedDelivery = session.get(Delivery.class, delivery.getDeliveryId());
        assertNotNull(fetchedDelivery);
        assertEquals("No", fetchedDelivery.getTaken());
    }

    @Test
    public void testUpdateDelivery() {
        Order order = createNewOrder();
        Courier courier = getAnyCourier();

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setCourier(courier);
        delivery.setDateArrived(LocalDateTime.now());
        delivery.setTaken("Yes");
        delivery.setPaymentMethod("Card");

        session.save(delivery);
        safeFlush();

        delivery.setPaymentMethod("Cash");
        safeFlush();

        Delivery updatedDelivery = session.get(Delivery.class, delivery.getDeliveryId());
        assertNotNull(updatedDelivery);
        assertEquals("Cash", updatedDelivery.getPaymentMethod());
    }

    @Test
    public void testDeleteDelivery() {
        Order order = createNewOrder();
        Courier courier = getAnyCourier();

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setCourier(courier);
        delivery.setDateArrived(LocalDateTime.now());
        delivery.setTaken("Yes");
        delivery.setPaymentMethod("Cash");

        session.save(delivery);
        safeFlush();

        session.delete(delivery);
        safeFlush();

        Delivery foundDelivery = session.get(Delivery.class, delivery.getDeliveryId());
        assertNull(foundDelivery);
    }
}
