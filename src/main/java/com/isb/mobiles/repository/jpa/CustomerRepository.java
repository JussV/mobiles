package com.isb.mobiles.repository.jpa;

import com.isb.mobiles.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
