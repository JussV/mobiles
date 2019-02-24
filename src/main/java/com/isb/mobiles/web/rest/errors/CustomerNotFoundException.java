package com.isb.mobiles.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class CustomerNotFoundException extends AbstractThrowableProblem {


    private static final long serialVersionUID = 1L;

    public CustomerNotFoundException() {
        super(ErrorConstants.CUSTOMER_NOT_FOUND_TYPE, "Customer not found", Status.BAD_REQUEST);
    }

}
