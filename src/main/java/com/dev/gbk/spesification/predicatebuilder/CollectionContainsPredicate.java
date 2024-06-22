package com.dev.gbk.spesification.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;

public class CollectionContainsPredicate<T> extends BasePredicate<T> {

    public CollectionContainsPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(getCriteriaExpressionJoinKey(root),
                parseValue(getCriteriaObjectValue().toString(), builder));
    }

}