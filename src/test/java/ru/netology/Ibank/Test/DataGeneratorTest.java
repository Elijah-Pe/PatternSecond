package ru.netology.Ibank.Test ;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.Ibank.Data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.Ibank.Data.DataGenerator.Registration.getUser;
import static ru.netology.Ibank.Data.DataGenerator.getRandomLogin;
import static ru.netology.Ibank.Data.DataGenerator.getRandomPassword;

public class DataGeneratorTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLogin() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2").should(Condition.exactText("Личный кабинет"), Condition.visible);
    }

    @Test
    void shouldNotLogin() {
        var notRegisteredUser = getUser("active");

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(18))
                .shouldBe(Condition.visible);
    }

    @Test
    void blockedUserCheck() {
        var blocked = getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blocked.getLogin());
        $("[data-test-id='password'] input").setValue(blocked.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! " + "Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    @Test
    void wrongLoginCheck() {
        var user = getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(getRandomLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void wrongPasswordCheck() {
        var user = getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(getRandomPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }
}
