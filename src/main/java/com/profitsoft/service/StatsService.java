package com.profitsoft.service;

import com.profitsoft.xml.write.ItemHolder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The class that forms stats by a certain attribute.
 * Factually, it has the 'attributeStats' field which is
 * ConcurrentHashMap, that has the attribute value as a map key
 * and attribute occurrence count as a map value.
 *
 * @param <T> a data type. Could be Integer or String, depends on the attribute
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsService<T> {

    Map<T, Integer> attributeStats;
    List<ItemHolder<T>> statsResult;

    public StatsService() {
        this.attributeStats = new ConcurrentHashMap<>();
    }

    /**
     * Adds attribute occurrence to the map.
     * In case the map already has such attribute
     * value then it is put again with attribute value as a key
     * and the map value is a previous occurrence count plus one.
     * If the map does not have some attribute value, then it is put
     * to the map with 1 as a value. (It means it is a first occurrence).
     *
     * @param attribute attribute that needed
     */

    public synchronized void addAttributeOccurrence(T attribute) {
        if (attributeStats.containsKey(attribute)) {
            attributeStats.put(attribute, attributeStats.get(attribute) + 1);
        } else {
            attributeStats.put(attribute, 1);
        }
    }

    /**
     * Forms the result of attribute statistics.
     *
     * @return a sorted list of ItemHolder class instances,
     * that have 2 fields: value (attribute value) and count
     * (attribute occurrence).
     * ItemHolder class is needed for marshalling.
     */
    public List<ItemHolder<T>> getStatsResult() {
        this.statsResult = new ArrayList<>();
        attributeStats.forEach((value, count) -> statsResult.add(new ItemHolder<>(value, count)));

        this.statsResult = statsResult.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getCount(), o1.getCount()))
                .collect(Collectors.toList());

        return this.statsResult;
    }
}
