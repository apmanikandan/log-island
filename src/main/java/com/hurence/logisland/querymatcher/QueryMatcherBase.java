package com.hurence.logisland.querymatcher;

import com.hurence.logisland.event.Event;

import java.util.Collection;
import java.util.List;

/**
 * Created by fprunier on 15/04/16.
 */
public abstract class QueryMatcherBase {

    private List<MatchingRule> rules = null;

    public QueryMatcherBase(List<MatchingRule> rules) {
        this.rules = rules;
    }

    public abstract Collection<Event> process(Collection<Event> collection);
}
