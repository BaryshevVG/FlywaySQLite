package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "courier_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "courier_id")
    private Long courierId;

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 35)
    private String lastName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "delivery_type", nullable = false, length = 5)
    private String deliveryType;
}
