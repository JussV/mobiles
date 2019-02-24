package com.isb.mobiles.repository.jpa;

import com.isb.mobiles.domain.MobileSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MobileSubscriptionRepository extends JpaRepository<MobileSubscription, Integer> {
}
