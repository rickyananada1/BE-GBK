package com.dev.gbk.spesification.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import static com.dev.gbk.utils.AppConstants.PERCENT_SIGN;

public class StartsWithPredicate<T> extends BasePredicate<T> {

    public StartsWithPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.like(builder.upper(getCriteriaStringExpressionKey(root).as(String.class)),
                StringUtils.join(StringUtils.upperCase(getCriteriaObjectValue().toString()), PERCENT_SIGN));
    }
}