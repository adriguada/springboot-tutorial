package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;

import java.util.List;

public interface CustomerService {

    /**
     * @return {@link List} de {@link Customer}
     */
    List<Customer> findAll();

    /**
     *
     * @param id PK de la entidad
     * @return entidad {@link Customer}
     */
    Customer findById(Long id);

    /**
     * Encuentra un cliente por su nombre
     * @param name Nombre del cliente
     * @return entidad {@link Customer}
     */
    Customer findByName(String name);

    /**
     * Borrar un {@link Customer}
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    /**
     * Guardar o actualizar un {@link Customer}
     * @param customer dto con datos de la entidad
     */
    void save(CustomerDto customer) throws Exception;
}
