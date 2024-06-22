package com.dev.gbk.spesification;

import com.dev.gbk.spesification.entity.SearchCriteria;
import com.dev.gbk.spesification.parse.NestedParseCommand;
import com.dev.gbk.spesification.parse.ParseCommand;
import com.dev.gbk.spesification.parse.ReferencedCompoundParseCommand;
import com.dev.gbk.spesification.parse.ReferencedSimpleParseCommand;
import com.dev.gbk.spesification.parse.SimpleParseCommand;
import com.dev.gbk.spesification.predicatebuilder.SpecificationFactory;
import com.dev.gbk.utils.SpecificationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static com.dev.gbk.spesification.entity.SearchOperation.OR_PREDICATE_FLAG;

@Log4j2
public class SpecificationBuilderImpl<T> implements SpecificationBuilder<T> {

    private final ObjectMapper objectMapper;
    private final Class<? extends Serializable> idClazz;
    private static final List<ParseCommand> PARSE_COMMANDS = new ArrayList<>(Arrays.asList(
            new SimpleParseCommand(),
            new NestedParseCommand(),
            new ReferencedSimpleParseCommand(),
            new ReferencedCompoundParseCommand()));

    public SpecificationBuilderImpl(ObjectMapper objectMapper, Class<T> entityClazz) {
        this.objectMapper = objectMapper;
        this.idClazz = initializeIdClazz(entityClazz);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Serializable> initializeIdClazz(Class<T> entityClazz) {
        try {
            return ((Class<? extends Serializable>) ((ParameterizedType) entityClazz
                    .getGenericSuperclass()).getActualTypeArguments()[0]);
        } catch (Exception e) {
            log.warn("Could not determine entity generic id type, error: {}", e.getMessage());
        }
        return Serializable.class;
    }

    @Override
    public Optional<Specification<T>> parseAndBuild(String search) {
        if (StringUtils.isBlank(search))
            return Optional.empty();
        var params = new ArrayList<SearchCriteria>();
        var splitOperation = SpecificationUtils.determineSplitOperation(search).orElse(null);
        var isOrPredicate = StringUtils.equals(splitOperation, OR_PREDICATE_FLAG);
        var searchQueries = SpecificationUtils.splitSearchOperations(search, splitOperation);
        PARSE_COMMANDS.forEach(p -> params.addAll(p.parse(searchQueries, isOrPredicate)));
        return build(params);
    }

    private Optional<Specification<T>> build(List<SearchCriteria> params) {
        if (params.isEmpty())
            return Optional.empty();
        var specFactory = new SpecificationFactory<T>(objectMapper, idClazz);
        Specification<T> result = specFactory.getSpecification(params.get(0));
        for (var i = 1; i < params.size(); i++)
            result = params.get(i).isOrPredicate()
                    ? Objects.requireNonNull(Specification.where(result))
                            .or(specFactory.getSpecification(params.get(i)))
                    : Objects.requireNonNull(Specification.where(result))
                            .and(specFactory.getSpecification(params.get(i)));
        return Optional.of(result);
    }

    protected Class<? extends Serializable> getIdClazz() {
        return this.idClazz;
    }
}