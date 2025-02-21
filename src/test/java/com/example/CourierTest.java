package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourierTest extends AbstractTest {

    @Test
    public void testCreateCourier() {
        Courier courier = new Courier();
        //в классе Courier у Вас нет этих set методов
        courier.setFirstName("John");
        courier.setLastName("Doe");
        courier.setPhoneNumber("+7 912 345 6789");
        courier.setDeliveryType("Bike");

        session.persist(courier);
        session.flush();

        assertNotNull(courier.getCourierId());
    }

    @Test
    public void testReadCourier() {
        Courier courier = new Courier();
        courier.setFirstName("Alice");
        courier.setLastName("Smith");
        courier.setPhoneNumber("+7 900 111 2222");
        courier.setDeliveryType("Car");

        session.persist(courier);
        session.flush();
        session.clear();

        Courier foundCourier = session.get(Courier.class, courier.getCourierId());
        assertNotNull(foundCourier);
        assertEquals("Alice", foundCourier.getFirstName());
    }

    @Test
    public void testUpdateCourierPhoneNumber() {
        Courier courier = new Courier();
        courier.setFirstName("Bob");
        courier.setLastName("Brown");
        courier.setPhoneNumber("+7 955 666 7777");
        courier.setDeliveryType("Scooter");

        session.persist(courier);
        session.flush();
        session.clear();

        Courier foundCourier = session.get(Courier.class, courier.getCourierId());
        assertNotNull(foundCourier);
        foundCourier.setPhoneNumber("+7 999 888 7777");
        session.merge(foundCourier);
        session.flush();
        session.clear();

        Courier updatedCourier = session.get(Courier.class, courier.getCourierId());
        assertEquals("+7 999 888 7777", updatedCourier.getPhoneNumber());
    }

    @Test
    public void testDeleteCourier() {
        Courier courier = new Courier();
        courier.setFirstName("Charlie");
        courier.setLastName("White");
        courier.setPhoneNumber("+7 911 222 3333");
        courier.setDeliveryType("Walk");

        session.persist(courier);
        session.flush();
        session.clear();

        Courier foundCourier = session.get(Courier.class, courier.getCourierId());
        assertNotNull(foundCourier);

        session.remove(foundCourier);
        session.flush();
        session.clear();

        Courier deletedCourier = session.get(Courier.class, courier.getCourierId());
        assertNull(deletedCourier);
    }
}
