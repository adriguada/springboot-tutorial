package com.ccsw.tutorial.lending;

import com.ccsw.tutorial.lending.model.Lending;
import com.ccsw.tutorial.lending.model.LendingDto;
import com.ccsw.tutorial.lending.model.LendingSearchDto;
import org.springframework.data.domain.Page;

public interface LendingService {

    /**
     * Get a {@link Page} of {@link Lending} entities given a search criteria
     * @param dto Dto object holding the page and search criteria information
     * @return a {@link Page} of {@link Lending} entities
     */
    Page<Lending> findPage(LendingSearchDto dto);

    /**
     * Save or update a {@link Lending} entity
     * @param id PK of the entity to be updated
     * @param dto Dto object holding the entity information
     */
    void save(Long id, LendingDto dto) throws Exception;

    /**
     * Delete a {@link Lending} entity
     * @param id PK of the entity to be deleted
     */
    void delete(Long id) throws Exception;
}
