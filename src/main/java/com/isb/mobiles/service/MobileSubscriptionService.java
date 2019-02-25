package com.isb.mobiles.service;

import com.isb.mobiles.domain.MobileSubscription;
import com.isb.mobiles.domain.enumeration.Type;
import com.isb.mobiles.repository.jpa.MobileSubscriptionRepository;
import com.isb.mobiles.repository.search.MobileSubscriptionSearchRepository;
import com.isb.mobiles.service.dto.MobileSubscriptionDTO;
import com.isb.mobiles.service.mapper.CustomerMapper;
import com.isb.mobiles.service.mapper.MobileSubscriptionMapper;
import com.isb.mobiles.web.rest.errors.CustomerNotFoundException;
import com.isb.mobiles.web.rest.errors.MobileSubscriptionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class MobileSubscriptionService {

    private final MobileSubscriptionRepository mobileSubscriptionRepository;

    private final MobileSubscriptionSearchRepository mobileSubscriptionSearchRepository;

    private final MobileSubscriptionMapper mobileSubscriptionMapper;

    private final EntityManager entityManager;

    private final CustomerMapper customerMapper;

    private final CustomerService customerService;

    public MobileSubscriptionService(MobileSubscriptionRepository mobileSubscriptionRepository,
                                     MobileSubscriptionSearchRepository mobileSubscriptionSearchRepository,
                                     MobileSubscriptionMapper mobileSubscriptionMapper,
                                     EntityManager entityManager,
                                     CustomerMapper customerMapper,
                                     CustomerService customerService) {
        this.mobileSubscriptionRepository = mobileSubscriptionRepository;
        this.mobileSubscriptionSearchRepository = mobileSubscriptionSearchRepository;
        this.mobileSubscriptionMapper = mobileSubscriptionMapper;
        this.entityManager = entityManager;
        this.customerMapper = customerMapper;
        this.customerService = customerService;
    }

    /**
     * Get all mobile subscriptions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MobileSubscriptionDTO> findAll(Pageable pageable) {
        log.info("Request to get all Mobile Subscriptions");
        return mobileSubscriptionRepository.findAll(pageable)
                .map(mobileSubscriptionMapper::toDto);
    }


    /**
     * Get mobile subscription by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MobileSubscriptionDTO findById(Integer id) {
        log.info("Request to get Mobile Subscription : {}", id);

        Optional<MobileSubscription> mobSub = mobileSubscriptionRepository.findById(id);
        if (mobSub.isEmpty()) {
            throw new MobileSubscriptionNotFoundException(id);
        }
        return mobileSubscriptionMapper.toDto(mobSub.get());
    }

    /**
     * Search for mobile subscriptions corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MobileSubscriptionDTO> search(String query, Pageable pageable) {
        log.info("Request to search for a page of Mobile Subscriptions for query {}", query);

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
        log.info("Request to save Mobile Subscription: {}", mobileSubscriptionDTO);
        MobileSubscription mobileSubscription = mobileSubscriptionMapper.toEntity(mobileSubscriptionDTO);
        mobileSubscription = mobileSubscriptionRepository.save(mobileSubscription);
        entityManager.refresh(mobileSubscription);
        mobileSubscriptionSearchRepository.save(mobileSubscription);
        return mobileSubscriptionMapper.toDto(mobileSubscription);
    }

    /**
     * Update service type of mobile subscription.
     *
     * @param id of the mobile subscription
     * @param type to update
     * @return the updated entity
     */
    public MobileSubscriptionDTO updateType(Integer id, Type type) {
        log.debug("Request to update Mobile Subscription Type to be: {}", type);

        MobileSubscription subUpdated = mobileSubscriptionRepository.findById(id)
                .map(sub -> {
                    sub.setServiceType(type);
                    return sub;
                })
                .orElseThrow(() -> new MobileSubscriptionNotFoundException(id));

        entityManager.detach(subUpdated);
        MobileSubscription mobileSubscription = mobileSubscriptionRepository.save(subUpdated);;
        mobileSubscriptionSearchRepository.save(mobileSubscription);
        log.info("Changed Type for Mobile Subscription: {}", mobileSubscription);
        return mobileSubscriptionMapper.toDto(mobileSubscription);

    }

    /**
     * Update owner of mobile subscription.
     *
     * @param id of the mobile subscription
     * @param mobSub to update
     * @return the updated entity
     */
    public MobileSubscriptionDTO updateOwner(Integer id, MobileSubscriptionDTO mobSub) {
        log.debug("Request to update Mobile Subscription owner to be: {}", mobSub);

        MobileSubscription mobileSubscription = mobileSubscriptionMapper.toEntity(mobSub);

        if(mobileSubscription.getOwner() == null
                || !customerService.findById(mobileSubscription.getOwner().getId()).isPresent()) {

            if(mobileSubscription.getOwner() == null){
                log.error("Owner object is missing in the request body");
                throw new CustomerNotFoundException();
            }
            else{
                log.error("Owner object with id {} not in the db", mobileSubscription.getOwner().getId());
                throw new CustomerNotFoundException(mobileSubscription.getOwner().getId());
            }
        }

        MobileSubscription subUpdated = mobileSubscriptionRepository.findById(id)
                .map(sub -> {
                    sub.setOwner(mobileSubscription.getOwner());
                    return sub;
                })
                .orElseThrow(() -> new MobileSubscriptionNotFoundException(id));

        entityManager.detach(subUpdated);
        MobileSubscription result = mobileSubscriptionRepository.save(subUpdated);
        mobileSubscriptionSearchRepository.save(result);
        log.info("Changed owner of Mobile Subscription: {}", result);
        return mobileSubscriptionMapper.toDto(result);
    }

    /**
     * Update user of mobile subscription.
     *
     * @param id of the mobile subscription
     * @param mobSub to update
     * @return the updated entity
     */
    public MobileSubscriptionDTO updateUser(Integer id, MobileSubscriptionDTO mobSub) {
        log.info("Request to update Mobile Subscription owner/user {}", mobSub);

        MobileSubscription mobileSubscription = mobileSubscriptionMapper.toEntity(mobSub);

        if(mobileSubscription.getUser() == null
                || !customerService.findById(mobileSubscription.getUser().getId()).isPresent()) {

            if(mobileSubscription.getUser() == null){
                log.error("User object is missing in the request body");
                throw new CustomerNotFoundException();
            }
            else{
                log.error("User object with id {} not in the db", mobileSubscription.getUser().getId());
                throw new CustomerNotFoundException(mobileSubscription.getUser().getId());
            }
        }

        MobileSubscription subUpdated = mobileSubscriptionRepository.findById(id)
                .map(sub -> {
                    sub.setUser(mobileSubscription.getUser());
                    return sub;
                })
                .orElseThrow(() -> new MobileSubscriptionNotFoundException(id));

        entityManager.detach(subUpdated);
        MobileSubscription result = mobileSubscriptionRepository.save(subUpdated);
        mobileSubscriptionSearchRepository.save(result);
        log.info("Changed user of Mobile Subscription: {}", result);
        return mobileSubscriptionMapper.toDto(result);
    }

    /**
     * Delete the mobile subscription by id.
     *
     * @param id the id of the entity
     */
    public void delete(Integer id) {
        log.info("Request to delete Mobile Subscription : {}", id);
        mobileSubscriptionRepository.deleteById(id);
        mobileSubscriptionSearchRepository.deleteById(id);
    }

}
