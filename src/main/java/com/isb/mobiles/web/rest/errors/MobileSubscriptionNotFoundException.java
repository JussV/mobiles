package com.isb.mobiles.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class MobileSubscriptionNotFoundException extends AbstractThrowableProblem {


    private static final long serialVersionUID = 1L;

    public MobileSubscriptionNotFoundException() {
        super(ErrorConstants.MOBILE_SUBSCRIPTION_NOT_FOUND_TYPE, "Mobile Subscription not found", Status.NOT_FOUND);
    }

}
