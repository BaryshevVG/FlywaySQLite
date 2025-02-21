package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest extends AbstractTest {

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setMenuName("Pizza");
        product.setPrice(12.99);

        session.persist(product);
        session.flush();

        assertNotNull(product.getId());
    }

    @Test
    public void testReadProduct() {
        Product product = new Product();
        product.setMenuName("Burger");
        product.setPrice(5.99);

        session.persist(product);
        session.flush();
        session.clear();

        Product foundProduct = session.get(Product.class, product.getId());
        assertNotNull(foundProduct);
        assertEquals("Burger", foundProduct.getMenuName());
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product();
        product.setMenuName("Sushi");
        product.setPrice(15.99);

        session.persist(product);
        session.flush();

        product.setPrice(18.99);
        session.merge(product);
        session.flush();

        Product updatedProduct = session.get(Product.class, product.getId());
        assertNotNull(updatedProduct);
        assertEquals(18.99, updatedProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        product.setMenuName("Pasta");
        product.setPrice(10.99);

        session.persist(product);
        session.flush();

        session.remove(product);
        session.flush();

        Product deletedProduct = session.get(Product.class, product.getId());
        assertNull(deletedProduct);
    }
}
