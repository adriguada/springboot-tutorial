package com.ccsw.tutorial.lending;

import com.ccsw.tutorial.common.error.exceptions.DeleteResourceException;
import com.ccsw.tutorial.customer.CustomerServiceImpl;
import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import com.ccsw.tutorial.game.GameServiceImpl;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.lending.model.Lending;
import com.ccsw.tutorial.lending.model.LendingDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LendingTest {

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = -1L;

    @Mock
    LendingRepository lendingRepository;

    @Mock
    GameServiceImpl gameService;

    @Mock
    CustomerServiceImpl customerService;

    @Spy
    @InjectMocks
    LendingServiceImpl lendingService;

    // Delete

    @Test
    public void deleteNotExistingShouldInternalError() {
        when(lendingRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(DeleteResourceException.class, () -> lendingService.delete(NOT_EXISTING_ID));
    }

    @Test
    public void deleteExistingShouldDelete() throws Exception {
        when(lendingRepository.findById(EXISTING_ID)).thenReturn(Optional.of(mock(Lending.class)));

        lendingService.delete(EXISTING_ID);

        verify(lendingRepository).deleteById(EXISTING_ID);
    }

    // Save
    @Test
    public void saveNoIdShouldCreate() throws Exception {
        LendingDto dto = new LendingDto();
        dto.setLoanDate(LocalDate.of(2026, 1, 1));
        dto.setReturnDate(LocalDate.of(2026, 1, 14));

        Long EXISTS_ID = 1L;
        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_ID);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(EXISTS_ID);
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        when(gameService.findById(EXISTS_ID)).thenReturn(mock(Game.class));
        when(customerService.findById(EXISTS_ID)).thenReturn(mock(Customer.class));

        doNothing().when(lendingService).checkValidDatesOrder(any());
        doNothing().when(lendingService).checkDateRangeDays(any());
        doNothing().when(lendingService).checkCustomerLendingLimit(any());
        doNothing().when(lendingService).checkGameNotLent(any());

        ArgumentCaptor<Lending> captor = ArgumentCaptor.forClass(Lending.class);
        lendingService.save(null, dto);
        verify(lendingRepository).save(captor.capture());

        assertEquals(captor.getValue().getLoanDate(), dto.getLoanDate());
    }

    @Test
    public void saveWithIdShouldUpdate() throws Exception {
        LendingDto dto = new LendingDto();
        dto.setId(EXISTING_ID);

        Long EXISTS_ID = 1L;
        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_ID);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(EXISTS_ID);
        dto.setGame(gameDto);
        dto.setCustomer(customerDto);

        when(gameService.findById(EXISTS_ID)).thenReturn(mock(Game.class));
        when(customerService.findById(EXISTS_ID)).thenReturn(mock(Customer.class));

        Lending lending = mock(Lending.class);
        when(lendingRepository.findById(EXISTING_ID)).thenReturn(Optional.of(lending));

        doNothing().when(lendingService).checkValidDatesOrder(lending);
        doNothing().when(lendingService).checkDateRangeDays(lending);
        doNothing().when(lendingService).checkCustomerLendingLimit(lending);
        doNothing().when(lendingService).checkGameNotLent(lending);

        lendingService.save(EXISTING_ID, dto);
        verify(lendingRepository).save(lending);
    }

}
