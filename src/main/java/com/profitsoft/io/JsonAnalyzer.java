package com.profitsoft.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.profitsoft.exception.JsonFormatException;
import com.profitsoft.utils.Constants;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonAnalyzer<T> {
    String attributeName;
    List<T> attributeOccurrences;
    JsonParser parser;
    Pattern pattern = Pattern.compile("(\\w+)");
    Integer SEARCH_LIMIT = 5000;

    public JsonAnalyzer(String attributeName, File file) throws IOException {
        JsonFactory factory = new JsonFactory();
        parser = factory.createParser(file);
        this.attributeOccurrences = new ArrayList<>();
        this.attributeName = attributeName;
    }

    public List<T> analyse() throws IOException {
        if (parser.nextToken() != JsonToken.START_ARRAY) {
            throw new JsonFormatException("incorrect json array");
        }

        analyseHelp(0);

        return attributeOccurrences;
    }

    private void analyseHelp(Integer countIterator) throws RuntimeException, IOException {
        parser.nextToken();

        if (parser.currentToken() == JsonToken.END_ARRAY) {
            return;
        }
        if (parser.currentToken() != JsonToken.START_OBJECT ||
                Objects.equals(countIterator, SEARCH_LIMIT)) {
            throw new JsonFormatException("not correct json format");
        }
        parser.nextToken();

        while (parser.currentToken() != JsonToken.END_OBJECT) {
            if (parser.currentToken() != JsonToken.FIELD_NAME) {
                throw new JsonFormatException("not correct json format");
            }
            if (Objects.equals(parser.getCurrentName(), this.attributeName)) {
                handleRequiredAttributeValue();
            } else {
                handleAnyAttributeValue();
            }
        }

        analyseHelp(++countIterator);
    }

    private void handleRequiredAttributeValue() throws IOException {
        switch (parser.getCurrentName()) {
            case Constants.PUBLICATION_HOUSE:
                handlePublicationHouseAttribute();
                break;
            case Constants.YEAR_PUBLISHED:
                handleYearPublishedAttribute();
                break;
            case Constants.GENRE:
                handleGenreAttribute();
                break;
        }
        parser.nextToken();
    }

    private void handlePublicationHouseAttribute() throws IOException {
        if (parser.nextToken() == JsonToken.VALUE_STRING) {
            attributeOccurrences.add((T) parser.getText());
        } else {
            throw new JsonFormatException("Not correct format of data attribute publication_house");
        }
    }


    private void handleYearPublishedAttribute() throws IOException {
        if (parser.nextToken() == JsonToken.VALUE_NUMBER_INT) {
            attributeOccurrences.add((T) parser.getText());
        } else {
            throw new JsonFormatException("Not correct format of data attribute year_published");
        }
    }

    private void handleGenreAttribute() throws IOException {
        if (parser.nextToken() == JsonToken.VALUE_STRING) {
            Matcher matcher = pattern.matcher(parser.getText());
            while (matcher.find()) {
                attributeOccurrences.add((T) matcher.group());
            }
        } else {
            throw new JsonFormatException("Not correct format of data attribute year_published");
        }
    }

    private void handleAnyAttributeValue() throws IOException {
        if (parser.nextToken() == JsonToken.START_OBJECT) {
            while (parser.currentToken() != JsonToken.END_OBJECT) {
                parser.nextToken();
            }

            return;
        }
        if (parser.nextToken() == JsonToken.START_ARRAY) {
            while (parser.currentToken() != JsonToken.END_ARRAY) {
                parser.nextToken();
            }
        }
    }
}
