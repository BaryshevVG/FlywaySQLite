package com.example;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

    @Column(name = "date_arrived")
    private LocalDateTime dateArrived;

    @Column(name = "taken", nullable = false, length = 3)
    private String taken;

    @Column(name = "payment_method", length = 4)
    private String paymentMethod;

    // Конструктор без аргументов
    public Delivery() {}

    // Конструктор со всеми параметрами
    public Delivery(Long deliveryId, Order order, Courier courier, LocalDateTime dateArrived, String taken, String paymentMethod) {
        this.deliveryId = deliveryId;
        this.order = order;
        this.courier = courier;
        this.dateArrived = dateArrived;
        this.taken = taken;
        this.paymentMethod = paymentMethod;
    }

    // Геттеры и сеттеры
    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public LocalDateTime getDateArrived() {
        return dateArrived;
    }

    public void setDateArrived(LocalDateTime dateArrived) {
        this.dateArrived = dateArrived;
    }

    public String getTaken() {
        return taken;
    }

    public void setTaken(String taken) {
        this.taken = taken;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
