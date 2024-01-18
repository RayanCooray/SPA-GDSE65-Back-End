package com.ijse.gdse.spagdse65backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CustomerDto {
    private String customer_id;
    private String name;
    private String address;
    private double salary;
}
