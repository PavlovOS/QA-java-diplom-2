package org.example.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrder {
    private String[] ingredients;

    public CreateOrder() {}

    public CreateOrder(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
