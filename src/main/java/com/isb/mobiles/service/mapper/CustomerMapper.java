package com.isb.mobiles.service.mapper;

import com.isb.mobiles.domain.Customer;
import com.isb.mobiles.service.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer>{

   // @Mapping(source = "id", target = "")
   // Customer toEntity(final CustomerDTO dto);

}
