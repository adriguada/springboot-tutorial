package com.ccsw.tutorial.lending;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.customer.CustomerService;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.lending.model.Lending;
import com.ccsw.tutorial.lending.model.LendingDto;
import com.ccsw.tutorial.lending.model.LendingSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class LendingServiceImpl implements LendingService {

    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private CustomerService customerService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Lending> findPage(LendingSearchDto dto) {
        LendingSpecification customerSpec = new LendingSpecification(new SearchCriteria("customer.id", ":", dto.getCustomerId()));
        LendingSpecification gameSpec = new LendingSpecification(new SearchCriteria("game.id", ":", dto.getGameId()));
        LendingSpecification dateLowerEqual = new LendingSpecification(new SearchCriteria("loanDate", "<:", dto.getDate()));
        LendingSpecification dateGreaterEqual = new LendingSpecification(new SearchCriteria("returnDate", ">:", dto.getDate()));

        Specification<Lending> spec = customerSpec.and(gameSpec).and(dateLowerEqual).and(dateGreaterEqual);
        return lendingRepository.findAll(spec, dto.getPageableRequest().getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void save(Long id, LendingDto dto) throws Exception {
        Lending lending;
        if (id == null)
            lending = new Lending();
        else
            lending = lendingRepository.findById(id).orElse(null);

        BeanUtils.copyProperties(dto, lending, "id", "game", "customer");

        // Comprobar que loanDate < returnDate
        checkValidDatesOrder(lending);

        // Comprobar que la reserva es de menos de 14 días
        checkDateRangeDays(lending);

        lending.setCustomer(customerService.findById(dto.getCustomer().getId()));
        lending.setGame(gameService.findById(dto.getGame().getId()));

        // Comprobar que el juego no está reservado para nadie más en el rango de días deseado
        checkGameNotLent(lending);

        // Comprobar que el cliente no tiene reservados dos juegos en un día
        checkCustomerLendingLimit(lending);

        lendingRepository.save(lending);
    }

    public void checkDateRangeDays(Lending lending) throws Exception {
        if (ChronoUnit.DAYS.between(lending.getLoanDate(), lending.getReturnDate()) > 14) {
            throw new Exception("Cannot lend a game for more than 14 days");
        }
    }

    public void checkValidDatesOrder(Lending lending) throws Exception {
        if (lending.getReturnDate().isBefore(lending.getLoanDate())) {
            throw new Exception("Return date should be greater than loan start date");
        }
    }

    public void checkCustomerLendingLimit(Lending lending) throws Exception {
        LendingSpecification sameCustomer = new LendingSpecification(new SearchCriteria("customer.id", ":", lending.getCustomer().getId()));

        LendingSpecification loanDateGe = new LendingSpecification(new SearchCriteria("loanDate", ">:", lending.getLoanDate()));
        LendingSpecification loanDateLe = new LendingSpecification(new SearchCriteria("loanDate", "<:", lending.getReturnDate()));

        LendingSpecification returnDateGe = new LendingSpecification(new SearchCriteria("returnDate", ">:", lending.getLoanDate()));
        LendingSpecification returnDateLe = new LendingSpecification(new SearchCriteria("returnDate", "<:", lending.getReturnDate()));

        LendingSpecification loanDateLeLoanDate = new LendingSpecification(new SearchCriteria("loanDate", "<:", lending.getLoanDate()));
        LendingSpecification returnDateGeReturnDate = new LendingSpecification(new SearchCriteria("returnDate", ">:", lending.getReturnDate()));

        Specification<Lending> spec = sameCustomer.and((loanDateGe.and(loanDateLe)).or(returnDateGe.and(returnDateLe)).or(loanDateLeLoanDate.and(returnDateGeReturnDate)));
        List<Lending> customerLendings = lendingRepository.findAll(spec);

        // Igual tiene una solución con sliding window para reducir de O(n2) a O(n),
        // pero tengo algo de prisa
        for (LocalDate day = lending.getLoanDate(); !day.isAfter(lending.getReturnDate()); day = day.plusDays(1)) {
            int maxOverlap = 0;
            for (Lending l : customerLendings) {
                if (!(l.getLoanDate().isAfter(day) || l.getReturnDate().isBefore(day))) {
                    maxOverlap++;
                }
            }

            if (maxOverlap >= 2)
                throw new Exception("Customer cannot have more games lent in this time period");
        }
    }

    /**
     * Check that a game has not been lent in a period of time
     * @param lending {@link Lending} entity with time period and game info
     * @throws Exception if a lending for a game in the specified time period is found
     */
    public void checkGameNotLent(Lending lending) throws Exception {
        LendingSpecification sameGame = new LendingSpecification(new SearchCriteria("game.id", ":", lending.getGame().getId()));

        LendingSpecification loanDateGe = new LendingSpecification(new SearchCriteria("loanDate", ">:", lending.getLoanDate()));
        LendingSpecification loanDateLe = new LendingSpecification(new SearchCriteria("loanDate", "<:", lending.getReturnDate()));

        LendingSpecification returnDateGe = new LendingSpecification(new SearchCriteria("returnDate", ">:", lending.getLoanDate()));
        LendingSpecification returnDateLe = new LendingSpecification(new SearchCriteria("returnDate", "<:", lending.getReturnDate()));

        LendingSpecification loanDateLeLoanDate = new LendingSpecification(new SearchCriteria("loanDate", "<:", lending.getLoanDate()));
        LendingSpecification returnDateGeReturnDate = new LendingSpecification(new SearchCriteria("returnDate", ">:", lending.getReturnDate()));

        Specification<Lending> spec = sameGame.and((loanDateGe.and(loanDateLe)).or(returnDateGe.and(returnDateLe)).or(loanDateLeLoanDate.and(returnDateGeReturnDate)));

        List<Lending> gameLendings = lendingRepository.findAll(spec);
        if (!gameLendings.isEmpty())
            throw new Exception("Game is already lent in this time period");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {
        if (lendingRepository.findById(id).isPresent())
            lendingRepository.deleteById(id);

        else
            throw new Exception("Lending not found");
    }
}
