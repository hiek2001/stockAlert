package org.hanghae.stockalert.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("0")
    private int quantity;

    @ColumnDefault("0")
    private int restock_rounds; // 재입고 회차


    public void reStock(int quantity) {
        this.quantity += quantity;
        this.restock_rounds++;
    }

    public void sell(int quantity) {
        if(this.quantity < 0) {
            throw new IllegalArgumentException("Stock amount exceeds stock limit");
        }
        this.quantity -= quantity;
    }

    public boolean isSoldOut() {
        return this.quantity == 0;
    }
}