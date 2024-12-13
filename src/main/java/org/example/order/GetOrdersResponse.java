package org.example.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetOrdersResponse {
    private boolean success;
    private List<OrderInformation> orders;
    private int total;
    private int totalToday;
}
