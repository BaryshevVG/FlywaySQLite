package com.example;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "orders_products")
@Where(clause = "order_id IS NOT NULL AND product_id IS NOT NULL")
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public OrderProduct() {}

    public OrderProduct(Order order, Product product, int quantity) {
        this.id = new OrderProductId(order.getOrderId(), product.getId());
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderProductId getId() {
        return id;
    }

    public void setId(OrderProductId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
