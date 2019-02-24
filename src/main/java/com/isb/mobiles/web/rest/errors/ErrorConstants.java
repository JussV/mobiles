package com.isb.mobiles.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {
    public static final String PROBLEM_BASE_URL = "https://test.com/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CUSTOMER_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/customer-not-found");
    public static final URI CUSTOMER_MISSING_TYPE = URI.create(PROBLEM_BASE_URL + "/customer-object-missing");
    public static final URI MOBILE_SUBSCRIPTION_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/mobile-subscription-not-found");

    private ErrorConstants() {}

}
