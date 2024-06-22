package com.dev.gbk.spesification.parse;

import java.util.List;

import com.dev.gbk.spesification.entity.SearchCriteria;

public interface ParseCommand {

    /**
     * Parses and constructs a List of SearchCriteria objects.
     * 
     * @param search        String[]
     * @param isOrPredicate boolean
     * @return List<SearchCriteria>
     */
    List<SearchCriteria> parse(String[] search, boolean isOrPredicate);

}