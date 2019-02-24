package com.isb.mobiles.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class MobileSubscriptionNotFoundException extends AbstractThrowableProblem {


    private static final long serialVersionUID = 1L;

    public MobileSubscriptionNotFoundException(int id) {
        super(ErrorConstants.MOBILE_SUBSCRIPTION_NOT_FOUND_TYPE, "Mobile Subscription not found for id " + id, Status.NOT_FOUND);
    }

}
