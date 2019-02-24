package com.isb.mobiles.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class CustomerNotFoundException extends AbstractThrowableProblem {


    private static final long serialVersionUID = 1L;

    public CustomerNotFoundException() {
        super(ErrorConstants.CUSTOMER_MISSING_TYPE, "Customer object is missing", Status.NOT_FOUND);
    }

    public CustomerNotFoundException(int id) {
        super(ErrorConstants.CUSTOMER_NOT_FOUND_TYPE, "Customer not found where id is " + id, Status.NOT_FOUND);
    }

}
