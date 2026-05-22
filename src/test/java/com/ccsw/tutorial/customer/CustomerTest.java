package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.common.error.exceptions.DeleteResourceException;
import com.ccsw.tutorial.common.error.exceptions.ValidationException;
import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerTest {

    private static final Long EXISTING_CUSTOMER_ID = 1L;
    private static final Long NOT_EXISTING_CUSTOMER_ID = 0L;
    private static final String EXISTING_NAME = "Javier";
    private static final String NOT_EXISTS_NAME = "Adrian";

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    // Customer find

    @Test
    public void findExistingIdShouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setId(EXISTING_CUSTOMER_ID);

        when(customerRepository.findById(EXISTING_CUSTOMER_ID)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findById(EXISTING_CUSTOMER_ID);

        assertNotNull(foundCustomer);
        assertEquals(EXISTING_CUSTOMER_ID, foundCustomer.getId());
    }

    @Test
    public void findNonExistingIdShouldReturnNull() {
        when(customerRepository.findById(NOT_EXISTING_CUSTOMER_ID)).thenReturn(Optional.empty());

        Customer foundCustomer = customerService.findById(NOT_EXISTING_CUSTOMER_ID);

        assertNull(foundCustomer);
    }

    @Test
    public void findAllShouldReturnAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> foundCustomers = customerService.findAll();

        assertNotNull(foundCustomers);
        assertEquals(customers.size(), foundCustomers.size());
    }

    @Test
    public void findByNameShouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setName(EXISTING_NAME);

        when(customerRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findByName(EXISTING_NAME);

        assertNotNull(foundCustomer);
        assertEquals(EXISTING_NAME, foundCustomer.getName());
    }

    @Test
    public void findNotExistingNameShouldReturnNull() {
        when(customerRepository.findByName(NOT_EXISTS_NAME)).thenReturn(Optional.empty());

        Customer foundCustomer = customerService.findByName(NOT_EXISTS_NAME);

        assertNull(foundCustomer);
    }

    // Customer save

    @Test
    public void saveNotExistingCustomerWithNotExistingNameShouldCreateCustomer() throws Exception {
        CustomerDto dto = new CustomerDto();
        dto.setName(NOT_EXISTS_NAME);

        ArgumentCaptor<Customer> customer = ArgumentCaptor.forClass(Customer.class);
        when(customerRepository.findByName(NOT_EXISTS_NAME)).thenReturn(Optional.empty());

        customerService.save(dto);

        verify(customerRepository).save(customer.capture());

        assertEquals(NOT_EXISTS_NAME, customer.getValue().getName());
    }

    @Test
    public void saveNotExistingCustomerWithExistingNameShouldNotCreateCustomer() throws Exception {
        CustomerDto dto = new CustomerDto();
        dto.setName(EXISTING_NAME);

        when(customerRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(mock(Customer.class)));

        assertThrows(ValidationException.class, () -> customerService.save(dto));
    }

    @Test
    public void saveExistingCustomerWithNotExistingNameShouldUpdateCustomer() throws Exception {
        CustomerDto dto = new CustomerDto();
        dto.setName(NOT_EXISTS_NAME);
        dto.setId(EXISTING_CUSTOMER_ID);

        when(customerRepository.findByName(NOT_EXISTS_NAME)).thenReturn(Optional.empty());

        Customer customer = mock(Customer.class);

        when(customerRepository.findById(EXISTING_CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.save(dto);
        verify(customerRepository).save(customer);
    }

    @Test
    public void saveExistingCustomerWithExistingNameShouldNotUpdateCustomer() throws Exception {
        CustomerDto dto = new CustomerDto();
        dto.setName(EXISTING_NAME);
        dto.setId(EXISTING_CUSTOMER_ID);

        Customer foundByName = mock(Customer.class);

        when(customerRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(foundByName));

        assertThrows(ValidationException.class, () -> customerService.save(dto));
    }

    // Customer delete

    @Test
    public void deleteExistingCustomerShouldDeleteCustomer() throws Exception {
        when(customerRepository.findById(EXISTING_CUSTOMER_ID)).thenReturn(Optional.of(mock(Customer.class)));

        customerService.delete(EXISTING_CUSTOMER_ID);
        verify(customerRepository).deleteById(EXISTING_CUSTOMER_ID);
    }

    @Test
    public void deleteNonExistingCustomerShouldThrowException() throws Exception {
        when(customerRepository.findById(NOT_EXISTING_CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(DeleteResourceException.class, () -> customerService.delete(NOT_EXISTING_CUSTOMER_ID));
    }
}
