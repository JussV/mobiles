package com.isb.mobiles.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.isb.mobiles.domain.enumeration.Type;
import com.isb.mobiles.service.view.View;
import lombok.Data;

/**
 * A DTO for the MobileSubscription Entity
 */
@Data
public class MobileSubscriptionDTO {

    private Integer id;

    @JsonView(View.MsisdnOwner.class)
    private String msisdn;

    @JsonView(View.MsisdnOwner.class)
    private CustomerDTO owner;

    private CustomerDTO user;

    private Type serviceType;

    private Long serviceStartDate;

}
