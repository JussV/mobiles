package com.isb.mobiles.service;

import com.isb.mobiles.domain.MobileSubscription;
import com.isb.mobiles.domain.enumeration.Type;
import com.isb.mobiles.repository.jpa.MobileSubscriptionRepository;
import com.isb.mobiles.repository.search.MobileSubscriptionSearchRepository;
import com.isb.mobiles.service.dto.MobileSubscriptionDTO;
import com.isb.mobiles.service.mapper.CustomerMapper;
import com.isb.mobiles.service.mapper.MobileSubscriptionMapper;
import com.isb.mobiles.web.rest.errors.MobileSubscriptionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Slf4j
@Transactional
public class MobileSubscriptionService {

    private final MobileSubscriptionRepository mobileSubscriptionRepository;

    private final MobileSubscriptionSearchRepository mobileSubscriptionSearchRepository;

    private final MobileSubscriptionMapper mobileSubscriptionMapper;

    private final EntityManager entityManager;

    private final CustomerMapper customerMapper;

    public MobileSubscriptionService(MobileSubscriptionRepository mobileSubscriptionRepository,
                                     MobileSubscriptionSearchRepository mobileSubscriptionSearchRepository,
                                     MobileSubscriptionMapper mobileSubscriptionMapper,
                                     EntityManager entityManager,
                                     CustomerService customerService,
                                     CustomerMapper customerMapper) {
        this.mobileSubscriptionRepository = mobileSubscriptionRepository;
        this.mobileSubscriptionSearchRepository = mobileSubscriptionSearchRepository;
        this.mobileSubscriptionMapper = mobileSubscriptionMapper;
        this.entityManager = entityManager;
        this.customerMapper = customerMapper;
    }

    /**
     * Get all mobile subscriptions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MobileSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mobile Subscriptions");
        return mobileSubscriptionRepository.findAll(pageable)
                .map(mobileSubscriptionMapper::toDto);
    }

    /**
     * Search for mobile subscriptions corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MobileSubscriptionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Mobile Subscriptions for query {}", query);

        QueryBuilder queryObject = QueryBuilders.boolQuery()
                .must(QueryBuilders.queryStringQuery(query).field("msisdn"));

        return mobileSubscriptionSearchRepository.search(queryObject, pageable)
                .map(mobileSubscriptionMapper::toDto);

    }

    /**
     * Save a mobile subscription.
     *
     * @param mobileSubscriptionDTO the entity to save
     * @return the persisted entity
     */
    public MobileSubscriptionDTO save(MobileSubscriptionDTO mobileSubscriptionDTO) {
        log.debug("Request to save Mobile Subscription: {}", mobileSubscriptionDTO);

        MobileSubscription mobileSubscription = mobileSubscriptionMapper.toEntity(mobileSubscriptionDTO);
        mobileSubscription = mobileSubscriptionRepository.save(mobileSubscription);
        mobileSubscriptionSearchRepository.save(mobileSubscription);
        entityManager.refresh(mobileSubscription);
        return mobileSubscriptionMapper.toDto(mobileSubscription);
    }

    /**
     * Update service type of mobile subscription.
     *
     * @param id of the mobile subscription
     * @param type to update
     * @return the updated entity
     */
    public MobileSubscriptionDTO updateType(Integer id, String type) {
        log.debug("Request to update Mobile Subscription Type to be: {}", type);

        try {
            MobileSubscription subUpdated = mobileSubscriptionRepository.findById(id)
                    .map(sub -> {
                        sub.setServiceType(Type.valueOf(type));
                        log.debug("Changed Type for Mobile Subscription: {}", sub);
                        return sub;
                    })
                    .orElseThrow(MobileSubscriptionNotFoundException::new);

            MobileSubscription mobileSubscription = mobileSubscriptionRepository.save(subUpdated);
            return mobileSubscriptionMapper.toDto(mobileSubscription);

        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex);
        }

    }

    /**
     * Update owner/user of mobile subscription.
     *
     * @param id of the mobile subscription
     * @param mobSub to update
     * @return the updated entity
     */
    public MobileSubscriptionDTO update(Integer id, MobileSubscriptionDTO mobSub) {
        log.debug("Request to update Mobile Subscription owner/user {}", mobSub);

        MobileSubscription subUpdated = mobileSubscriptionRepository.findById(id)
                .map(sub -> {
                    if (mobSub.getOwner() != null) {
                        sub.setOwner(customerMapper.toEntity(mobSub.getOwner()));
                    };
                    if (mobSub.getUser() != null) {
                        sub.setUser(customerMapper.toEntity(mobSub.getUser()));
                    };
                    log.debug("Changed Information for Mobile Subscription: {}", sub);
                    return sub;
                })
                .orElseThrow(MobileSubscriptionNotFoundException::new);

        MobileSubscription mobileSubscription = mobileSubscriptionRepository.save(subUpdated);
        return mobileSubscriptionMapper.toDto(mobileSubscription);

    }

}
