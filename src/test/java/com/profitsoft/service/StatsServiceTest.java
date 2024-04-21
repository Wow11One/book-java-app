package com.profitsoft.service;


import com.profitsoft.xml.write.ItemHolder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static org.assertj.core.api.Assertions.assertThat;

class StatsServiceTest {

    @Test
    public void testThatStatsAreSorted() {
        List<String> genreValues = List.of("Comedy", "Tragedy", "Comedy", "Fiction");
        StatsService<String> statsService = new StatsService<>();
        genreValues.forEach(statsService::addAttributeOccurrence);

        List<ItemHolder<String>> statsResult = statsService.getStatsResult();

        assertThat(statsResult)
                .isNotEmpty()
                .isSortedAccordingTo(((o1, o2) -> Integer.compare(o2.getCount(), o1.getCount())));
    }

    @Test
    public void testThatStatsAreCorrectlyCalculated() {
        /*
            In this method I get stats result, that is a list of
            ItemHolder class instances. That class has 2 field: count (amount of
            occurrences in list) and value.
            This class is needed for  xml marshalling.
            Before that I created a list with some mock values and by stream api
            I grouped it and counted occurrences of each element in the list and
            converted to the map after all.
            Eventually, I checked whether each instance of ItemHolder class has
            the same count value in the map.
         */
        List<String> genreValues = List.of("Comedy", "Tragedy", "Comedy", "Fiction");
        Map<String, Integer> expectedResult = genreValues.stream()
                .collect(Collectors.groupingBy(Function.identity(),
                        collectingAndThen(Collectors.counting(), Long::intValue)));

        StatsService<String> statsService = new StatsService<>();
        genreValues.forEach(statsService::addAttributeOccurrence);
        List<ItemHolder<String>> statsResult = statsService.getStatsResult();

        assertThat(statsResult).isNotEmpty();
        statsResult.forEach(item ->
                assertThat(expectedResult.get(item.getValue())).isEqualTo(item.getCount()));
    }

    @Test
    public void testThatStatsAreCorrectlyCalculatedForIntegers() {
        List<Integer> genreValues = List.of(1991, 1923, 1991, 1991, 1987, 1917);
        Map<Integer, Integer> expectedResult = genreValues.stream()
                .collect(Collectors.groupingBy(Function.identity(),
                        collectingAndThen(Collectors.counting(), Long::intValue)));

        StatsService<Integer> statsService = new StatsService<>();
        genreValues.forEach(statsService::addAttributeOccurrence);
        List<ItemHolder<Integer>> statsResult = statsService.getStatsResult();

        assertThat(statsResult).isNotEmpty();
        statsResult.forEach(item ->
                assertThat(expectedResult.get(item.getValue())).isEqualTo(item.getCount()));
    }
}