package com.profitsoft.json.parse;

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

/**
 * The class that analyses certain json file.
 * The most important method here is 'analyse()'.
 * It parses json file by using jackson streaming api
 * object by object. In case file format is not correct
 * then JsonParseException or custom JsonFormatException
 * can occur. If required attribute is missing in object,
 * then no error occurs. Also, it is important to mention
 * that there is a 'SEARCH_LIMIT' field that limits the amount
 * of token for analysing in case the file format is broken.
 *
 * @param <T> a data type. Could be Integer or String, depends on the attribute
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonAnalyzer<T> {

    String attributeName;
    List<T> attributeOccurrences;
    JsonParser parser;
    Pattern pattern;
    Integer SEARCH_LIMIT;

    public JsonAnalyzer(String attributeName, File file) throws IOException {
        JsonFactory factory = new JsonFactory();
        this.parser = factory.createParser(file);
        this.attributeName = attributeName;
        this.attributeOccurrences = new ArrayList<>();
        this.pattern = Pattern.compile("(\\w+)");
        this.SEARCH_LIMIT = 5000;
    }

    /**
     * Starts analysis of a file using jackson streaming api.
     * It checks whether first token of the file is the array start
     * and then stars a recursive helper method that handles parsing
     * of the array objects.
     *
     * @return a list of values of a specified attribute. It counts
     * all occurrences of attribute in file.
     * @throws IOException         In case file format is not correct or file
     *                             does not exist
     * @throws JsonFormatException In case file format is not correct
     */
    public List<T> analyse() throws IOException {
        if (parser.nextToken() != JsonToken.START_ARRAY) {
            throw new JsonFormatException(parser, "incorrect json array");
        }

        analyseHelp(0);

        return attributeOccurrences;
    }

    /**
     * Helper method that recursively parse objects in the json array
     * from a file. This method also delegates json object fields
     * handling to other methods. The method stops if the current
     * token is the end of array or search limit is exceeded.
     *
     * @param countIterator counts how many iteration method does.
     *                      In case 'countIterator' equals to 'SEARCH_LIMIT',
     *                      method stops.
     * @throws IOException         when file format is not correct or does not exist.
     * @throws JsonFormatException In case file format is not correct.
     */
    private void analyseHelp(Integer countIterator) throws IOException {
        parser.nextToken();

        if (parser.currentToken() == JsonToken.END_ARRAY) {
            return;
        }
        if (parser.currentToken() != JsonToken.START_OBJECT ||
                Objects.equals(countIterator, SEARCH_LIMIT)) {
            throw new JsonFormatException(parser, "not correct json format");
        }
        parser.nextToken();

        while (parser.currentToken() != JsonToken.END_OBJECT) {
            if (parser.currentToken() != JsonToken.FIELD_NAME) {
                throw new JsonFormatException(parser, "not correct json format");
            }
            if (Objects.equals(parser.getCurrentName(), this.attributeName)) {
                handleRequiredAttributeValue();
            } else {
                handleAnyAttributeValue();
            }
        }

        analyseHelp(++countIterator);
    }

    /**
     * Handles values of a field that is specified by user.
     *
     * @throws IOException when file format is not correct or does not exist.
     */
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

    /**
     * Handles value of a 'publication_house' field in case it is specified by user.
     *
     * @throws IOException when file format is not correct or does not exist.
     */
    private void handlePublicationHouseAttribute() throws IOException {
        if (parser.nextToken() == JsonToken.VALUE_STRING) {
            attributeOccurrences.add((T) parser.getText());
        } else {
            throw new JsonFormatException(parser,
                    "Not correct format of data attribute publication_house");
        }
    }

    /**
     * Handles value of a 'year_published' field in case it is specified by user.
     *
     * @throws IOException when file format is not correct or does not exist.
     */
    private void handleYearPublishedAttribute() throws IOException {
        if (parser.nextToken() == JsonToken.VALUE_NUMBER_INT) {
            attributeOccurrences.add((T) (Integer) parser.getIntValue());
        } else {
            throw new JsonFormatException(parser,
                    "Not correct format of data attribute year_published");
        }
    }

    /**
     * Handles value of a 'genre' field in case it is specified by user.
     *
     * @throws IOException when file format is not correct or does not exist.
     */
    private void handleGenreAttribute() throws IOException {
        if (parser.nextToken() == JsonToken.VALUE_STRING) {
            Matcher matcher = pattern.matcher(parser.getText());
            while (matcher.find()) {
                attributeOccurrences.add((T) matcher.group());
            }
        } else {
            throw new JsonFormatException(parser,
                    "Not correct format of data attribute year_published");
        }
    }

    /**
     * Handles value of fields that is not required by user and statistics calculation.
     * Just iterates to the next field token.
     *
     * @throws IOException when file format is not correct or does not exist.
     */
    private void handleAnyAttributeValue() throws IOException {
        if (parser.nextToken() == JsonToken.START_OBJECT) {
            while (parser.currentToken() != JsonToken.END_OBJECT) {
                parser.nextToken();
            } // in case field is included object

            return;
        }
        if (parser.nextToken() == JsonToken.START_ARRAY) {
            while (parser.currentToken() != JsonToken.END_ARRAY) {
                parser.nextToken();
            }
        }
    }
}
