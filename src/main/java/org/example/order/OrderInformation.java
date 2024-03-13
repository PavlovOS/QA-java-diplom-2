package org.example.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInformation {
    private String _id;
    private IngredientsList ingredients;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
}
