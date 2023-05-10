package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserAccount {
    private String email;
    private String password;
    private String name;

    @Override
    public String toString() {
        return String.format("Аккаунт пользователя. email: %s; Пароль: %s; Имя: %s.", email, password, name);
    }
}
