package org.example.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {
    private String email;
    private String password;

    public Login() {
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
