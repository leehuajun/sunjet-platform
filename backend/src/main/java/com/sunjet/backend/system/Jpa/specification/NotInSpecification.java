package com.sunjet.backend.system.Jpa.specification;

import javax.persistence.criteria.*;

public class NotInSpecification<T> extends AbstractSpecification<T> {
    private String property;
    private Object[] values;

    public NotInSpecification(String property, Object[] values) {
        this.property = property;
        this.values = values;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        return from.get(field).in(values).not();
    }
}
