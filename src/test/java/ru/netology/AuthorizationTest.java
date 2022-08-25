package ru.netology;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import java.sql.DriverManager;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

public class AuthorizationTest {
    DataHelper.AuthInfo firstUser = DataHelper.getAuthInfoFirstUser();
    DataHelper.AuthInfo secondUser = DataHelper.getAuthInfoSecondUser();

    @SneakyThrows
    public String getVerificationCode() {
        String verificationCode = null;
        var verificationCodeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1;";
        try (
                var conn = DriverManager
                        .getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
                var getVerCode = conn.prepareStatement(verificationCodeSQL)
        ) {
            try (var verCode = getVerCode.executeQuery()) {
                if (verCode.next()) {
                    verificationCode = verCode.getString("code");
                }
            }
        }
        return verificationCode;
    }

    @BeforeEach
    void setupTest() {
        open("http://localhost:9999");
    }

    @AfterAll
    @SneakyThrows
    static void clearDataBase() {
        var clearAuthCodes = "DELETE FROM auth_codes;";
        var clearCards = "DELETE FROM cards;";
        var clearCardTransactions = "DELETE FROM card_transactions;";
        var clearUsers = "DELETE FROM users;";
        try (
                var conn = DriverManager
                        .getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
                var clearAuthCodesStmt = conn.prepareStatement(clearAuthCodes);
                var clearCardsStmt = conn.prepareStatement(clearCards);
                var clearCardTransactionsStmt = conn.prepareStatement(clearCardTransactions);
                var clearUsersStmt = conn.prepareStatement(clearUsers)
        ) {
            clearAuthCodesStmt.executeUpdate();
            clearCardsStmt.executeUpdate();
            clearCardTransactionsStmt.executeUpdate();
            clearUsersStmt.executeUpdate();
        }
    }

    @Test
    void shouldAuthorizationWithFirstUser() {

        new LoginPage().validLogin(firstUser);
        new VerificationPage().validVerify(getVerificationCode());
    }

    @Test
    void shouldAuthorizationWithSecondUser() {

        new LoginPage().validLogin(secondUser);
        new VerificationPage().validVerify(getVerificationCode());
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
        new LoginPage().validLogin(secondUser);
        new VerificationPage().invalidVerify(DataHelper.getInvalidVerificationCodeFor(secondUser).getVerificationCode());
    }
    @Test
    void shouldBlockedInvalidVerificationCodeWithFirstUser() {
        new LoginPage().validLogin(firstUser);
        VerificationPage verificationPage = new VerificationPage();
        for (int i = 0; i < 2; i++) {
            verificationPage.invalidVerify(DataHelper.getInvalidVerificationCodeFor(firstUser).getVerificationCode());
        }
        verificationPage.blockedForInputTripleInvalidVerificationCode(DataHelper.getInvalidVerificationCodeFor(firstUser)
                .getVerificationCode());
    }
}

