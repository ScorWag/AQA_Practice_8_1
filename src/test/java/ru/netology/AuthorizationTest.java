package ru.netology;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataBaseManager;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class AuthorizationTest {
    DataHelper.AuthInfo firstUser = DataHelper.getAuthInfoFirstUser();
    DataHelper.AuthInfo secondUser = DataHelper.getAuthInfoSecondUser();

    @BeforeEach
    void setupTest() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void clearBase() {
        DataBaseManager.clearDataBase();
    }

    @Test
    void shouldAuthorizationWithFirstUser() {

        new LoginPage().validLogin(firstUser).validVerify(DataBaseManager.getVerificationCode());
    }

    @Test
    void shouldAuthorizationWithSecondUser() {

        new LoginPage().validLogin(secondUser).validVerify(DataBaseManager.getVerificationCode());
    }

    @Test
    void shouldErrorInvalidUser() {
        DataHelper.AuthInfo invalidUser = DataHelper.getInvalidAuthInfo();

        new LoginPage().invalidAuthInfo(invalidUser);

    }

    @Test
    void shouldErrorInvalidLoginOnly() {
        DataHelper.AuthInfo userWithInvalidLogin = DataHelper.getInvalidLoginAuthInfo();

        new LoginPage().invalidAuthInfo(userWithInvalidLogin);

    }

    @Test
    void shouldErrorInvalidPasswordOnly() {
        DataHelper.AuthInfo userWithInvalidPassword = DataHelper.getInvalidPasswordAuthInfo();

        new LoginPage().invalidAuthInfo(userWithInvalidPassword);

    }

    @Test
    void shouldErrorInvalidVerificationCodeWithSecondUser() {
        new LoginPage().validLogin(secondUser).invalidVerify(DataHelper.getInvalidVerificationCodeFor(secondUser)
                .getVerificationCode());
    }
    @Test
    void shouldBlockedInvalidVerificationCodeWithFirstUser() {
        VerificationPage verificationPage = new LoginPage().validLogin(firstUser);
        for (int i = 0; i < 2; i++) {
            verificationPage.invalidVerify(DataHelper.getInvalidVerificationCodeFor(firstUser).getVerificationCode());
        }
        verificationPage.blockedForInputTripleInvalidVerificationCode(DataHelper
                .getInvalidVerificationCodeFor(firstUser).getVerificationCode());
    }
}

