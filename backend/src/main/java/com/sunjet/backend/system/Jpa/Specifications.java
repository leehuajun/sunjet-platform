package com.sunjet.backend.system.Jpa;

import static javax.persistence.criteria.Predicate.BooleanOperator.AND;
import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

/**
 * Created by SUNJET_WS on 2017/7/19.
 */
public class Specifications {

    public static <T> PredicateBuilder<T> and() {
        return new PredicateBuilder<>(AND);
    }

    public static <T> PredicateBuilder<T> or() {
        return new PredicateBuilder<>(OR);
    }
}
