package com.isb.mobiles.web.rest.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalando.problem.StatusType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private StatusType status;
    private int errorCode;
    private String message;
    private String detail;
}
