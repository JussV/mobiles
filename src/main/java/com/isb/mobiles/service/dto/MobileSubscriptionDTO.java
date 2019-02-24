package com.isb.mobiles.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.isb.mobiles.domain.enumeration.Type;
import com.isb.mobiles.service.view.View;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * A DTO for the MobileSubscription Entity
 */
@Data
@ApiModel(description = "Class representing a mobile subscription.")
public class MobileSubscriptionDTO {

    @ApiModelProperty(notes = "${swagger.subscription.id}", hidden = true)
    private Integer id;

    @JsonView(View.MsisdnOwner.class)
    @ApiModelProperty(notes = "${swagger.subscription.msisdn}", required = true)
    private String msisdn;

    @JsonView(View.MsisdnOwner.class)
    @ApiModelProperty(notes = "${swagger.subscription.owner}", required = true)
    private CustomerDTO owner;

    @ApiModelProperty(notes = "${swagger.subscription.user}", required = true)
    private CustomerDTO user;

    @ApiModelProperty(notes = "${swagger.subscription.type}", required = true)
    private Type serviceType;

    @ApiModelProperty(notes = "${swagger.subscription.start-date}", required = true, hidden = true)
    private Long serviceStartDate;

}
