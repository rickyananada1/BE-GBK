package com.dev.gbk.spesification.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dev.gbk.spesification.entity.SearchSection;

import static com.dev.gbk.spesification.entity.SearchOperation.OPERATION_EXPRESSION;
import static com.dev.gbk.spesification.entity.SearchOperation.REGEX_SIMPLE_SUFFIX;

public class ReferencedSimpleParseCommand extends BaseParseCommand {

    private static final String REFERENCED_SIMPLE_REGEX_PATTERN = String.join("", "(\\w+\\p{Punct}\\w+?)(",
            OPERATION_EXPRESSION, REGEX_SIMPLE_SUFFIX);

    public ReferencedSimpleParseCommand() {
        super(Pattern.compile(REFERENCED_SIMPLE_REGEX_PATTERN));
    }

    @Override
    protected SearchSection process(Matcher matcher) {
        return new SearchSection(matcher.group(1),
                matcher.group(2),
                matcher.group(4),
                matcher.group(3),
                matcher.group(5));
    }
}