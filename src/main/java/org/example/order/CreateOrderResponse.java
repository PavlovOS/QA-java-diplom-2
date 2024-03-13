package org.example.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderResponse {
    private String name;
    private OrderForCreate order;
    private boolean success;
}
