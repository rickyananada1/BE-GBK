package com.dev.gbk.spesification.predicatebuilder;

import com.dev.gbk.utils.SpecificationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;

public class EqualityPredicate<T> extends BasePredicate<T> {

    public EqualityPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        var searchValue = getCriteriaObjectValue().toString();
        if (SpecificationUtils.isValueNullKey(searchValue))
            return builder.isNull(getCriteriaStringExpressionKey(root));
        return builder.equal(getCriteriaStringExpressionKey(root), parseValue(searchValue, builder));
    }
}