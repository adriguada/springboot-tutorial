package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    /**
     * Encuentra un cliente por su nombre
     * @param name Nombre del cliente
     * @return {@link Optional} de {@link Customer}
     */
    Optional<Customer> findByName(String name);
}
