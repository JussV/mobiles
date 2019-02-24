package com.isb.mobiles.web.rest.exceptions;

import com.isb.mobiles.web.rest.errors.BadRequestAlertException;
import com.isb.mobiles.web.rest.errors.CustomerNotFoundException;
import com.isb.mobiles.web.rest.errors.MobileSubscriptionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zalando.problem.Status;

@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(NumberFormatException ex){

        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(Status.BAD_REQUEST)
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .detail("Request parameter could not be parsed to integer")
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({MobileSubscriptionNotFoundException.class})
    protected ResponseEntity<Object> handleSubscriptionNotFound(MobileSubscriptionNotFoundException ex){

        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(ex.getStatus())
                .errorCode(ex.getStatus().getStatusCode())
                .message(ex.getLocalizedMessage())
                .detail(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CustomerNotFoundException.class})
    protected ResponseEntity<Object> handleCustomerNotFound(CustomerNotFoundException ex){

        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(ex.getStatus())
                .errorCode(ex.getStatus().getStatusCode())
                .message(ex.getLocalizedMessage())
                .detail(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({BadRequestAlertException.class})
    protected ResponseEntity<Object> handleBadRequest(BadRequestAlertException ex){

        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(ex.getStatus())
                .errorCode(ex.getStatus().getStatusCode())
                .message(ex.getLocalizedMessage())
                .detail(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public void defaultExceptionHandler() {
        // Nothing to do
    }
}
