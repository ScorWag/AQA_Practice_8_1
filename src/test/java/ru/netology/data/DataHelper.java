package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfoFirstUser() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getAuthInfoSecondUser() {
        return new AuthInfo("petya", "123qwerty");
    }
    public static AuthInfo getInvalidAuthInfo() {
        return new AuthInfo(
                new Faker().name().username(), new Faker().internet().password());
    }

    public static AuthInfo getInvalidLoginAuthInfo() {
        return new AuthInfo(
                new Faker().name().username(), "qwerty123");
    }
    public static AuthInfo getInvalidPasswordAuthInfo() {
        return new AuthInfo(
                "vasya", new Faker().internet().password());
    }

    @Value
    public static class VerificationCode {
        private String verificationCode;
    }

    public static VerificationCode getInvalidVerificationCodeFor(AuthInfo info) {
        String code = new Faker().number().digits(6);
        return new VerificationCode(code);
    }
}
