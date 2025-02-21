package com.example;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest extends AbstractTest {

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Иван");
        customer.setLastName("Иванов");
        customer.setPhoneNumber("1234567890");
        customer.setDistrict("Центральный");
        customer.setStreet("Ленина");
        customer.setHouse(10);
        customer.setApartment(5);

        session.save(customer);
        session.flush();

        assertNotNull(customer.getId());
    }

    @Test
    public void testReadCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Мария");
        customer.setLastName("Петрова");
        customer.setPhoneNumber("0987654321");
        customer.setDistrict("Южный");
        customer.setStreet("Гагарина");
        customer.setHouse(20);
        customer.setApartment(10);

        session.save(customer);
        session.flush();

        Customer fetchedCustomer = session.get(Customer.class, customer.getId());
        assertNotNull(fetchedCustomer);
        assertEquals("Мария", fetchedCustomer.getFirstName());
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Алексей");
        customer.setLastName("Сидоров");
        customer.setPhoneNumber("5555555555");
        customer.setDistrict("Западный");
        customer.setStreet("Победы");
        customer.setHouse(15);
        customer.setApartment(3);

        session.save(customer);
        session.flush();

        customer.setPhoneNumber("1111111111");
        session.update(customer);
        session.flush();

        Customer updatedCustomer = session.get(Customer.class, customer.getId());
        assertEquals("1111111111", updatedCustomer.getPhoneNumber());
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Ольга");
        customer.setLastName("Николаева");
        customer.setPhoneNumber("7777777777");
        customer.setDistrict("Северный");
        customer.setStreet("Дружбы");
        customer.setHouse(7);
        customer.setApartment(2);

        session.save(customer);
        session.flush();

        session.delete(customer);
        session.flush();

        Customer deletedCustomer = session.get(Customer.class, customer.getId());
        assertNull(deletedCustomer);
    }
}
