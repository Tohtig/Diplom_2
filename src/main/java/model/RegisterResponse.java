package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterResponse {

    private Boolean success;
    private String message;
    private User user;
    private String accessToken;
    private String refreshToken;
}
