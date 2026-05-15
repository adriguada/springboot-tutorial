package com.ccsw.tutorial.lending;

import com.ccsw.tutorial.lending.model.Lending;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LendingRepository extends JpaRepository<Lending, Long>, JpaSpecificationExecutor<Lending> {

    @Override
    @EntityGraph(attributePaths = { "game", "customer" })
    Page<Lending> findAll(Specification<Lending> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = { "game", "customer" })
    List<Lending> findAll(Specification<Lending> spec);
}
