package com.isb.mobiles.service.mapper;

import com.isb.mobiles.domain.MobileSubscription;
import com.isb.mobiles.service.dto.MobileSubscriptionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity MobileSubscription and its DTO MobileSubscriptionDTO
 */
@Mapper(componentModel = "spring", uses = {})
public interface MobileSubscriptionMapper extends EntityMapper<MobileSubscriptionDTO, MobileSubscription>{
}
