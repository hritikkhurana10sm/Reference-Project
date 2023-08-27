package com.amanboora.auth.api.generic;

public interface Constants {

    String AUTHORIZATION = "Authorization";

    String INVALID_TOKEN_MESSAGE = "Expired or invalid JWT token";

    String SIGNIN_URL = "/api/auth/signin";

    String SIGNUP_URL = "/api/auth/signup";

    String AUTH_FAILED_MESSAGE = "Authentication failed!";

    String EMPTY_USERNAME = "Username is required";

    String EMPTY_PASSWORD = "Password is required";

    String EMPTY_EMAIL = "Email-Address is required";

    String EMPTY_CONFIRMPASSWORD = "Confirm Password is required";

    String EMPTY_NAME = "Name is required";

    String INVALID_CREDENTIALS = "Invalid Credentials passed";

    String NOTUNIQUE_USERNAME = "Username is already in use";

    String NOTUNIQUE_EMAIL = "Email-Address is already in use";

    String UNCONFIRMED_PASSWORD = "Password and Confirm Password do not match";

    String UNPROCESSABLE_REQUEST = "Unprocessable Request Data";

    String USER_REGISTERED = "Registered Successfully";

    String LOGIN_SUCCESSFULLY = "Login Successfully";

    String USER_ACTIVATED = "Activation Successful";

    String USER_NOTFOUND = "User Not Found";

    String ACCESS_DENIED = "Access Denied";

    String OLD_ACTIVATION="User is already Activated";
}
