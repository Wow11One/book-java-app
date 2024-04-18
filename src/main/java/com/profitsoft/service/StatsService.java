package com.profitsoft.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class StatsService<T> {

    Map<T, Integer> attributeStats;

    public StatsService() {
        this.attributeStats = new ConcurrentHashMap<>();
    }

    public synchronized void addAttributeOccurrence(T attribute) {
        if (attributeStats.containsKey(attribute)) {
            attributeStats.put(attribute, attributeStats.get(attribute) + 1);
        } else {
            attributeStats.put(attribute, 1);
        }
    }
}
