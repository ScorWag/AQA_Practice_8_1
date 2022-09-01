package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    SelenideElement loginInput = $("[data-test-id=login] input");
    SelenideElement passwordInput = $("[data-test-id=password] input");
    SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");

    private void login(DataHelper.AuthInfo info) {
        loginInput.setValue(info.getLogin());
        passwordInput.setValue(info.getPassword());
        loginButton.click();
    }
    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        login(info);
        return new VerificationPage();
    }

    public void invalidAuthInfo(DataHelper.AuthInfo info) {
        login(info);
        errorNotification.shouldBe(visible).shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }
}
