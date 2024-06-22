package com.dev.gbk.utils;

import org.apache.commons.lang3.StringUtils;

import com.dev.gbk.spesification.entity.SearchOperation;
import com.dev.gbk.spesification.entity.SearchSection;

import java.util.Optional;
import static com.dev.gbk.spesification.entity.SearchOperation.OR_PREDICATE_FLAG;
import static com.dev.gbk.utils.AppConstants.*;

public final class SpecificationUtils {

    public static Optional<String> determineSplitOperation(String search) {
        if (StringUtils.contains(search, COMA))
            return Optional.of(COMA);
        if (StringUtils.contains(search, OR_PREDICATE_FLAG))
            return Optional.of(OR_PREDICATE_FLAG);
        return Optional.empty();
    }

    public static String[] splitSearchOperations(String search, String operator) {
        if (operator == null)
            return new String[] { search };
        return StringUtils.split(search, operator);
    }

    public static boolean isValueNullKey(String search) {
        return StringUtils.equals(search, "null");
    }

    public static SearchOperation resolveSearchOperation(SearchSection searchSection) {
        var parsedSearchOperation = SearchOperation
                .getSimpleOperation(StringUtils.substring(searchSection.getOperation(), 0, 1));
        if (parsedSearchOperation == SearchOperation.EQUALITY) {
            boolean startWithAsterisk = StringUtils.contains(searchSection.getPrefix(), ASTERISK);
            boolean endWithAsterisk = StringUtils.contains(searchSection.getSuffix(), ASTERISK);
            if (startWithAsterisk && endWithAsterisk)
                return SearchOperation.CONTAINS;
            if (!startWithAsterisk && !endWithAsterisk)
                return SearchOperation.EQUALITY;
            if (startWithAsterisk)
                return SearchOperation.ENDS_WITH;
            return SearchOperation.STARTS_WITH;
        }
        return parsedSearchOperation;
    }

    public static boolean isKeyCompound(String key) {
        return StringUtils.contains(key, DOT);
    }

    public static String[] getCompoundKeys(String key) {
        return StringUtils.split(key, DOT, 2);
    }

    private SpecificationUtils() {

    }

}