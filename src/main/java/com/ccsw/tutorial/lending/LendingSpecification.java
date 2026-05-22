package com.ccsw.tutorial.lending;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.lending.model.Lending;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LendingSpecification implements Specification<Lending> {

    private SearchCriteria criteria;

    public LendingSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    // clean codeado
    @Override
    public Predicate toPredicate(Root<Lending> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getValue() == null) {
            return null;
        }

        Path<?> path = getPath(root);
        String operation = criteria.getOperation();

        switch (operation) {
        case ":": {
            if (path.getJavaType() == String.class) {
                return builder.like(path.as(String.class), "%" + criteria.getValue() + "%");
            }
            return builder.equal(path, criteria.getValue());
        }

        case ">:":
            return builder.greaterThanOrEqualTo((Path<LocalDate>) path, (LocalDate) criteria.getValue());

        case "<:":
            return builder.lessThanOrEqualTo((Path<LocalDate>) path, (LocalDate) criteria.getValue());

        case "between":
            return builder.between((Path<LocalDate>) path, (LocalDate) criteria.getValue(), (LocalDate) criteria.getValue2());

        case "<>":
            return builder.notEqual(path, criteria.getValue());

        default:
            return null;
        }
    }

    private <T> Path<T> getPath(Root<Lending> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<T> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

}
