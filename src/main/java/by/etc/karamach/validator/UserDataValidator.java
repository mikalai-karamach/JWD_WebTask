package by.etc.karamach.validator;

import by.etc.karamach.bean.User;

public class UserDataValidator {
    private final static String EMAIL_REGEX =
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static boolean isValidEmail(String email) {
        return (((email != null) && (!email.isEmpty())) && (email.matches(EMAIL_REGEX)));
    }

    public static boolean isValidPassword(String password) {
        return (password != null) && (!password.isEmpty());
    }

    public static boolean isValidName(String name) {
        return (name != null) && (!name.isEmpty());
    }

    public static boolean isValidUserData(User user) {
        return ((isValidPassword(user.getPassword()) &&
                isValidEmail(user.getEmail())) &&
                (isValidName(user.getName())));
    }
}
