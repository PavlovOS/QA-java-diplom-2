package org.example.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateResponse {
    private boolean success;
    private User user;
}
