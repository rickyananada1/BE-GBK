package com.dev.gbk.spesification.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;

public class GreaterThanOrEqualPredicate<T> extends BasePredicate<T> {

    public GreaterThanOrEqualPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.greaterThanOrEqualTo(getCriteriaExpressionKey(root),
                parseValue(getCriteriaObjectValue().toString(), builder));
    }

}