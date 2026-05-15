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

    @Override
    public Predicate toPredicate(Root<Lending> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase(">:") && criteria.getValue() != null) {
            Path<LocalDate> path = getPath(root);
            return builder.greaterThanOrEqualTo(path, (LocalDate) criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase("<:") && criteria.getValue() != null) {
            Path<LocalDate> path = getPath(root);
            return builder.lessThanOrEqualTo(path, (LocalDate) criteria.getValue());
        }
        return null;
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
