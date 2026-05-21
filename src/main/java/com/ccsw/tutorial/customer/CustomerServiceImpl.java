package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.common.error.exceptions.DeleteResourceException;
import com.ccsw.tutorial.common.error.exceptions.ValidationException;
import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> findAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer findByName(String name) {
        return customerRepository.findByName(name).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {
        if (findById(id) != null)
            customerRepository.deleteById(id);
        else
            throw new DeleteResourceException("Customer not found");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(CustomerDto dto) throws Exception {
        // Comprobar que el nombre no existe
        String name = dto.getName();
        if (findByName(name) != null && !Objects.equals(findByName(name).getId(), dto.getId()))
            throw new ValidationException("Name is already registered");

        Customer customer;
        if (dto.getId() == null)
            customer = new Customer();
        else
            customer = findById(dto.getId());

        customer.setName(name);
        customerRepository.save(customer);
    }
}
