package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/customer")
@CrossOrigin(origins = "*")
@Tag(name = "Customer", description = "API of Customer")
public class CustomerController {

    CustomerService customerService;
    ModelMapper mapper;

    public CustomerController(CustomerService customerService, ModelMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }

    /**
     * Find all customers
     * @return a List of {@link CustomerDto} entities
     */
    @GetMapping(path = "")
    @Operation(summary = "Find", description = "Method that return a list of Customers")
    public List<CustomerDto> findAll() {
        return customerService.findAll().stream().map(e -> mapper.map(e, CustomerDto.class)).collect(Collectors.toList());
    }

    /**
     * Delete a {@link Customer} entity
     * @param id PK of the entity
     * @throws Exception
     */
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete", description = "Method that deletes a Customer")
    public void delete(@PathVariable("id") Long id) throws Exception {
        customerService.delete(id);
    }

    /**
     * Save or update a {@link Customer} entity
     * @param customerDto DTO object holding the entity's information
     * @throws Exception
     */
    @PutMapping(path = "")
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Customer")
    public void save(@RequestBody CustomerDto customerDto) throws Exception {
        customerService.save(customerDto);
    }
}
