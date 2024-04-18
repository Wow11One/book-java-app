package com.profitsoft.utils;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JacksonXmlRootElement(localName = "statistics")
@Getter
public class ResultMapHolder<T> {

    @JacksonXmlElementWrapper(useWrapping = false, localName = "item")
    private List<Item> items;

    public ResultMapHolder(Map<T, Integer> resultMap) {
        try {
            this.items = new ArrayList<>();
            resultMap.forEach((value, count) -> items.add(new Item(value, count)));

            this.items = items.stream()
                    .sorted((o1, o2) -> Integer.compare(o2.count, o1.count))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @AllArgsConstructor
    private class Item {
        @JacksonXmlProperty
        private T value;

        @JacksonXmlProperty
        private Integer count;
    }
}