package com.example.leftover_rescue.MODEL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing an inventory item.
 */
@Entity
@Table(name = "inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Relationship with the User

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;  // Relationship with the Ingredient

    @Column(nullable = false)
    private String quantity; // Quantity of the ingredient

    @Column(nullable = false)
    private String unit; // Unit of the quantity (e.g., grams, cups)

    @Column
    private LocalDateTime expiryDate;  // Expiry date for the ingredient in the inventory
}
