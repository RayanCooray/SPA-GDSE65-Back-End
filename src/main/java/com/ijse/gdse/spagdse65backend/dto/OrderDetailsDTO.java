package com.ijse.gdse.spagdse65backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailsDTO {
    private String order_id;
    private String Item_Code;
    private double price;
    private int qty;
}
