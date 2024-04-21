package com.profitsoft.json.parse;

import com.fasterxml.jackson.core.JsonParseException;
import com.profitsoft.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class JsonAnalyzerTest {

    @ParameterizedTest
    @MethodSource("provideIncorrectFormatFiles")
    public void testThatAnalyseMethodThrowsErrorWhenFileIsNotCorrect(File file)
            throws IOException {
        JsonAnalyzer<String> jsonAnalyzer = new JsonAnalyzer<>("genre", file);
        assertThatThrownBy(jsonAnalyzer::analyse)
                .isInstanceOf(JsonParseException.class);
    }

    @ParameterizedTest
    @MethodSource("provideCorrectFormatFiles")
    public void testThatAnalyseMethodDoesNotThrowErrorWhenFileIsCorrect(File file)
            throws IOException {
        JsonAnalyzer<String> jsonAnalyzer = new JsonAnalyzer<>("genre", file);

        assertThat(jsonAnalyzer.analyse())
                .isInstanceOf(List.class)
                .isNotEmpty();
    }

    @Test
    public void testThatFileWithCorrectJsonCorrectlyAnalysesGenreField()
            throws IOException {
        /*
            in this test a file with correct json created and saved
            after that we assert that analyse method correctly
            analysed json field genre
         */
        List<String> expectedGenres = List.of("Dystopian", "Fiction", "Comedy");
        String jsonGenreField = String.join(",", expectedGenres);
        String json = """
                [
                  {
                    "title": "Don Quijote",
                    "publication_house": "Folio",
                    "year_published": 1949,
                    "genre": "%s"
                  }
                ]
                """.formatted(jsonGenreField);
        File file = new File("src/test/resources/test-json-1.json");

        // used FileOutputStream, because object mapper adds escape chars to result file
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(json.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();

        JsonAnalyzer<String> jsonAnalyzer = new JsonAnalyzer<>(Constants.GENRE, file);

        assertThat(jsonAnalyzer.analyse())
                .isInstanceOf(List.class)
                .isNotEmpty()
                .containsExactlyElementsOf(expectedGenres);
    }

    @Test
    public void testThatFileWithCorrectJsonCorrectlyAnalysesYearPublishedField()
            throws IOException {
        List<Integer> expectedYears = List.of(1944, 1952);
        String json = """
                [
                  {
                    "title": "Don Quijote",
                    "publication_house": "Folio",
                    "year_published": %s,
                    "genre": "Comedy"
                  },
                  {
                    "title": "Don Quijote",
                    "publication_house": "Folio",
                    "year_published": %s,
                    "genre": "Comedy"
                  }
                ]
                """.formatted(expectedYears.get(0), expectedYears.get(1));

        File file = new File("src/test/resources/test-json-2.json");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(json.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();

        JsonAnalyzer<Integer> jsonAnalyzer = new JsonAnalyzer<>(Constants.YEAR_PUBLISHED, file);

        assertThat(jsonAnalyzer.analyse())
                .isInstanceOf(List.class)
                .isNotEmpty()
                .containsExactlyElementsOf(expectedYears);
    }

    @Test
    public void testThatFileWithCorrectJsonCorrectlyAnalysesPublicationHouseField()
            throws IOException {
        List<String> publicationHousesExpected = List.of("Folio", "ABABAGALAMAGA");
        String json = """
                [
                  {
                    "title": "Don Quijote",
                    "publication_house": "%s",
                    "year_published": "1944",
                    "genre": "Comedy"
                  },
                  {
                    "title": "Don Quijote",
                    "publication_house":"%s",
                    "year_published": 1944,
                    "genre": "Comedy"
                  }
                ]
                """.formatted(publicationHousesExpected.get(0),
                publicationHousesExpected.get(1));

        File file = new File("src/test/resources/test-json-3.json");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(json.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();

        JsonAnalyzer<String> jsonAnalyzer = new JsonAnalyzer<>(Constants.PUBLICATION_HOUSE, file);

        assertThat(jsonAnalyzer.analyse())
                .isInstanceOf(List.class)
                .isNotEmpty()
                .containsExactlyElementsOf(publicationHousesExpected);
    }

    @Test
    public void testThatIfRequiredFieldIsMissingThanObjec()
            throws IOException {
        /*
            in this test a file with correct json created and saved
            after that we assert that analyse method correctly
            analysed json field genre
         */
        List<String> expectedGenres = List.of("Dystopian", "Fiction", "Comedy");
        String jsonGenreField = String.join(",", expectedGenres);
        String json = """
                [
                  {
                    "title": "Don Quijote",
                    "publication_house": "Folio",
                    "year_published": 1949,
                    "genre": "%s"
                  }
                ]
                """.formatted(jsonGenreField);
        File file = new File("src/test/resources/test-json-1.json");

        // used FileOutputStream, because object mapper adds escape chars to the result file
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(json.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();

        JsonAnalyzer<String> jsonAnalyzer = new JsonAnalyzer<>(Constants.GENRE, file);

        assertThat(jsonAnalyzer.analyse())
                .isInstanceOf(List.class)
                .isNotEmpty()
                .containsExactlyElementsOf(expectedGenres);
    }

    private static Stream<File> provideCorrectFormatFiles() {
        return Arrays.stream(
                new File(Constants.PROJECT_PATH + "json/five-correct-files")
                        .listFiles()
        );
    }

    private static Stream<File> provideIncorrectFormatFiles() {
        return Arrays.stream(
                new File(Constants.PROJECT_PATH + "json/all-files-not-correct")
                        .listFiles()
        );
    }
}