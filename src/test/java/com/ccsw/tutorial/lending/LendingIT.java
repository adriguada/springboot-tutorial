package com.ccsw.tutorial.lending;

import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.customer.model.CustomerDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.lending.model.LendingDto;
import com.ccsw.tutorial.lending.model.LendingSearchDto;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LendingIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/lending";

    private static final int PAGE_SIZE = 5;
    private static final int TOTAL_LENDINGS = 7;

    private static final Long GAME_1_ID = 1L;
    private static final String GAME_1_TITLE = "On Mars";
    private static final Long CUSTOMER_1_ID = 1L;
    private static final String CUSTOMER_1_NAME = "David";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<LendingDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LendingDto>>() {
    };

    private LendingSearchDto searchPage(int i) {
        LendingSearchDto searchDto = new LendingSearchDto();
        searchDto.setPageableRequest(new PageableRequest(i, PAGE_SIZE));
        return searchDto;
    }

    // Find (page testing)

    @Test
    public void findFirstPageWithSize5ShouldReturn5Lendings() {
        LendingSearchDto searchDto = searchPage(0);

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LENDINGS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithSize5ShouldReturn2Lendings() {
        LendingSearchDto searchDto = searchPage(1);

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LENDINGS, response.getBody().getTotalElements());
        assertEquals(TOTAL_LENDINGS - PAGE_SIZE, response.getBody().getContent().size());
    }

    // Find (filter testing)

    @Test
    public void findWithFilterGameShouldReturnLendings() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setGameId(GAME_1_ID);

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals(GAME_1_TITLE, response.getBody().getContent().get(0).getGame().getTitle());
    }

    @Test
    public void findWithFilterCustomerShouldReturnLendings() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setCustomerId(CUSTOMER_1_ID);

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals(CUSTOMER_1_NAME, response.getBody().getContent().get(0).getCustomer().getName());
    }

    @Test
    public void findWithFilterGameAndCustomerShouldReturnLendings() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setCustomerId(CUSTOMER_1_ID);
        searchDto.setGameId(GAME_1_ID);

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(GAME_1_TITLE, response.getBody().getContent().get(0).getGame().getTitle());
        assertEquals(CUSTOMER_1_NAME, response.getBody().getContent().get(0).getCustomer().getName());
    }

    @Test
    public void findWithFilterGameNotExistsShouldReturnEmpty() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setGameId(-1L);

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    public void findWithFilterCustomerNotExistsShouldReturnEmpty() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setCustomerId(-1L);

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    public void findWithFilterDateInBetweenShouldReturnLendings() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setDate(LocalDate.of(2026, 5, 2));

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getTotalElements());
    }

    @Test
    public void findWithFilterDateEqualsFirstDayShouldReturnLendings() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setDate(LocalDate.of(2026, 4, 1));

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getTotalElements());
    }

    @Test
    public void findWithFilterDateEqualsLastDayShouldReturnLendings() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setDate(LocalDate.of(2026, 5, 25));

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void findWithFilterDateLowerThanFirstDayShouldEmpty() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setDate(LocalDate.of(2026, 4, 1).minusDays(1));

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    public void findWithFilterDateGreaterThanLastDayShouldEmpty() {
        LendingSearchDto searchDto = searchPage(0);
        searchDto.setDate(LocalDate.of(2026, 5, 25).plusDays(1));

        ResponseEntity<ResponsePage<LendingDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
    }

    // Save
    @Test
    public void saveMoreThan14DaysShouldInternalError() {
        LendingDto dto = new LendingDto();
        dto.setLoanDate(LocalDate.now());
        dto.setReturnDate(dto.getLoanDate().plusDays(15));

        ResponseEntity<?> res = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void saveReturnDateBeforeLendDateShouldInternalError() {
        LendingDto dto = new LendingDto();
        dto.setLoanDate(LocalDate.now());
        dto.setReturnDate(dto.getLoanDate().minusDays(1));

        ResponseEntity<?> res = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void saveLessThan14DaysNotLentGameShouldSave() {
        LendingDto dto = new LendingDto();
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);

        dto.setLoanDate(LocalDate.of(2020, 5, 1));
        dto.setReturnDate(dto.getLoanDate().plusDays(13));
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        ResponseEntity<?> res = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
    }

    @Test
    public void saveGameLentBeforeOverlapsRangeShouldInternalError() {
        // Create lending: '2020-01-01', '2020-01-14', game1, customer3

        LendingDto dto = new LendingDto();
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(3L);

        dto.setLoanDate(LocalDate.of(2020, 1, 1));
        dto.setReturnDate(dto.getLoanDate().plusDays(13));
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        // Try to create a lending that overlaps with the previous one for the same game
        dto = new LendingDto();
        gameDto = new GameDto();
        gameDto.setId(1L);
        customerDto = new CustomerDto();
        customerDto.setId(1L);

        dto.setLoanDate(LocalDate.of(2020, 1, 10));
        dto.setReturnDate(dto.getLoanDate().plusDays(13));
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        ResponseEntity<?> res = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void saveGameLentAfterOverlapsRangeShouldInternalError() {
        // Create lending: '2020-01-14', '2020-01-27', game1, customer3

        LendingDto dto = new LendingDto();
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(3L);

        dto.setLoanDate(LocalDate.of(2020, 1, 14));
        dto.setReturnDate(dto.getLoanDate().plusDays(13));
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        // Try to create a lending that overlaps with the previous one for the same game
        dto = new LendingDto();
        gameDto = new GameDto();
        gameDto.setId(1L);
        customerDto = new CustomerDto();
        customerDto.setId(1L);

        dto.setLoanDate(LocalDate.of(2020, 1, 1));
        dto.setReturnDate(dto.getLoanDate().plusDays(13));
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        ResponseEntity<?> res = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    // con análisis de valores límite
    @Test
    public void saveGameWith2overlappedLendingsShouldInternalError() {
        // Create two overlapped lendings for the same customer

        // Create lending: '2020-01-01', '2020-01-10', game1, customer1
        LendingDto dto = new LendingDto();
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);

        dto.setLoanDate(LocalDate.of(2020, 1, 1));
        dto.setReturnDate(LocalDate.of(2020, 1, 10));
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        // Create lending: '2020-01-10', '2020-01-20', game2, customer1
        dto = new LendingDto();
        gameDto = new GameDto();
        gameDto.setId(2L);
        customerDto = new CustomerDto();
        customerDto.setId(1L);

        dto.setLoanDate(LocalDate.of(2020, 1, 10));
        dto.setReturnDate(LocalDate.of(2020, 1, 20));
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        // Create lending: '2020-01-09', '2020-01-11', game3, customer1
        dto = new LendingDto();
        gameDto = new GameDto();
        gameDto.setId(3L);
        customerDto = new CustomerDto();
        customerDto.setId(1L);

        dto.setLoanDate(LocalDate.of(2020, 1, 9));
        dto.setReturnDate(LocalDate.of(2020, 1, 11));
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        ResponseEntity<?> res = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

}
