package com.profitsoft.xml.write;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The class that holds a pair of the attribute value and its count.
 * This class is created for marshalling.
 *
 * @param <T> a data type. Could be Integer or String, depends on attribute
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Getter
@JacksonXmlRootElement(localName = "item")
public class ItemHolder<T> {

    @JacksonXmlProperty
    T value;

    @JacksonXmlProperty
    Integer count;
}
