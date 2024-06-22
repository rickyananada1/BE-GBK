package com.dev.gbk.spesification.predicatebuilder;

import com.dev.gbk.utils.SpecificationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;

public class NegationPredicate<T> extends BasePredicate<T> {

    public NegationPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (SpecificationUtils.isValueNullKey(getCriteriaObjectValue().toString()))
            return builder.isNotNull(getCriteriaStringExpressionKey(root));
        return builder.notEqual(getCriteriaStringExpressionKey(root),
                parseValue(getCriteriaObjectValue().toString(), builder));
    }
}