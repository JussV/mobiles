package com.isb.mobiles.repository.search;

import com.isb.mobiles.domain.MobileSubscription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Mobile Subscription entity.
 */
public interface MobileSubscriptionSearchRepository extends ElasticsearchRepository<MobileSubscription, Integer> {

}
