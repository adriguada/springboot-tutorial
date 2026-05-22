package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/customer";

    public static final Long NOT_EXISTING_ID = -1L;
    public static final Long EXISTING_ID = 1L;
    public static final String EXISTING_NAME = "Javier";
    public static final String NOT_EXISTING_NAME = "Galileo";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<CustomerDto>> responseType = new ParameterizedTypeReference<>() {
    };

    // Customer find

    @Test
    public void findAllShouldReturnAllCustomers() {
        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response.getBody());
        assertEquals(8, response.getBody().size());
    }

    // Customer save

    @Test
    public void saveNotIdNotExistingNameShouldCreate() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(NOT_EXISTING_NAME);

        // Crear el customer
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(customerDto), responseType);

        // Comprobar que ha aumentado +1 el número de customers
        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response.getBody());
        assertEquals(9, response.getBody().size());

        // Buscar el customer insertado entre la respuesta con todos los customers
        CustomerDto insertedCustomer = response.getBody().stream().filter(c -> c.getName().equals(NOT_EXISTING_NAME)).findFirst().orElse(null);
        assertNotNull(insertedCustomer);
    }

    @Test
    public void saveExistingIdNotExistingNameShouldUpdate() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(NOT_EXISTING_NAME);
        customerDto.setId(EXISTING_ID);

        // Modificar el customer
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(customerDto), responseType);

        // Comprobar que el número de customers sigue igual
        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response.getBody());
        assertEquals(8, response.getBody().size());

        // Buscar el customer con el nuevo nombre entre los customers recibidos
        CustomerDto updtedCustomer = response.getBody().stream().filter(c -> c.getId().equals(EXISTING_ID)).findFirst().orElse(null);
        assertNotNull(updtedCustomer);
        assertEquals(NOT_EXISTING_NAME, updtedCustomer.getName());
    }

    @Test
    public void saveNotExistingIdExistingNameShouldInternalError() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(EXISTING_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(customerDto), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveExistingIdExistingNameShouldInternalError() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(EXISTING_NAME);
        customerDto.setId(EXISTING_ID);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(customerDto), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteShouldDeleteCustomer() {
        // Borrar customer
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTING_ID, HttpMethod.DELETE, null, Void.class);

        // Comprobar que la lista de customers ha reducido en 1
        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response.getBody());
        assertEquals(7, response.getBody().size());
    }

    @Test
    public void deleteNotExistingShouldInternalError() {
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTING_ID, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
