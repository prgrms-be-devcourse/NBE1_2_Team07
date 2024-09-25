package com.develetter.develetter.common;

public interface ResponseMessage {
    String SUCCESS = "Success";

    String VALIDATION_FAIL = "Validation failed";
    String DUPLICATE_ID = "Duplicate ID";

    String SIGN_IN_FAIL = "Login information mismatch";
    String CERTIFICATION_FAIL = "Certification failed";

    String MAIL_FAIL = "Failed to send email";
    String DATABASE_ERROR = "Database error";

}
