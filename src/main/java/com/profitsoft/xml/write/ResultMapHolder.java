package com.profitsoft.xml.write;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * The class that is needed to marshall statistics results.
 * It stores a list of ItemHolder class instances that will be
 * converted to xml.
 *
 * @param <T> a data type. Could be Integer or String, depends on attribute.
 */
@JacksonXmlRootElement(localName = "statistics")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
public class ResultMapHolder<T> {

    @JacksonXmlElementWrapper(useWrapping = false, localName = "item")
    List<ItemHolder<T>> item;
}