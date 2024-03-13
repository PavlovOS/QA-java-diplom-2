package org.example.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateResponse {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
}
