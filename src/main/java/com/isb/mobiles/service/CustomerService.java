package com.isb.mobiles.service;

import com.isb.mobiles.repository.jpa.CustomerRepository;
import com.isb.mobiles.repository.jpa.MobileSubscriptionRepository;
import com.isb.mobiles.repository.search.MobileSubscriptionSearchRepository;
import com.isb.mobiles.service.dto.CustomerDTO;
import com.isb.mobiles.service.mapper.CustomerMapper;
import com.isb.mobiles.service.mapper.MobileSubscriptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final EntityManager entityManager;

    public CustomerService(CustomerRepository customerRepository,
                                     CustomerMapper customerMapper,
                                     EntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.entityManager = entityManager;
    }

    /**
     * Get customer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findById(Integer id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::toDto);
    }



}
