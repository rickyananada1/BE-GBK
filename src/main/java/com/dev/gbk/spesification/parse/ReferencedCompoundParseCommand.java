package com.dev.gbk.spesification.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dev.gbk.spesification.entity.SearchSection;

import static com.dev.gbk.spesification.entity.SearchOperation.OPERATION_EXPRESSION;
import static com.dev.gbk.spesification.entity.SearchOperation.REGEX_NESTED_SUFFIX;

public class ReferencedCompoundParseCommand extends BaseParseCommand {

    private static final String REFERENCED_COMPOUND_REGEX_PATTERN = String.join("", "(\\w+\\p{Punct}\\w+?)(",
            OPERATION_EXPRESSION, REGEX_NESTED_SUFFIX);

    public ReferencedCompoundParseCommand() {
        super(Pattern.compile(REFERENCED_COMPOUND_REGEX_PATTERN));
    }

    @Override
    protected SearchSection process(Matcher matcher) {
        var value = String.join("", matcher.group(4), matcher.group(5), matcher.group(7));
        return new SearchSection(matcher.group(1),
                matcher.group(2),
                value,
                matcher.group(6),
                matcher.group(8));
    }
}