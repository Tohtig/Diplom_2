package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private String email;
    private String password;

    public Login(UserAccount account) {
        this.email = account.getEmail();
        this.password = account.getPassword();
    }

    @Override
    public String toString() {
        return String.format("Логин пользователя. email: %s; Пароль: %s.", email, password);
    }
}
