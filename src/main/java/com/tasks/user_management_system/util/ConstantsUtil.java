package com.tasks.user_management_system.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantsUtil {

    // error
    public static final String USER_NOT_FOUND_ERROR = "User not found";
    public static final String EMAIL_EXISTS_ERROR = "Email already exists";
    public static final String EMAIL_NOT_FOUND_ERROR = "Email not found";
    public static final String INVALID_CREDENTIALS_ERROR = "Invalid credentials";
    public static final String ERROR = "error";

    // user status
    public static final String ACTIVE_STATUS = "active";
}
