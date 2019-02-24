package com.isb.mobiles.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * A DTO for a Customer Entity
 */
@Data
@ApiModel(description = "Class representing a customer of mobile subscription.")
public class CustomerDTO {

    @ApiModelProperty(notes = "${swagger.customer.id}", required = true)
    private Integer id;

    @ApiModelProperty(notes = "${swagger.customer.first-name}", required = true, hidden = true)
    private String firstName;

    @ApiModelProperty(notes = "${swagger.customer.last-name}", required = true, hidden = true)
    private String lastName;

}
