package com.ijse.gdse.spagdse65backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ItemDto {
    private String item_code;
    private String description;
    private double unit_price;
    private int qty;
}
