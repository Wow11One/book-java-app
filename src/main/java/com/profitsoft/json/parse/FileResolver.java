package com.profitsoft.json.parse;


import com.profitsoft.Main;
import com.profitsoft.service.StatsService;
import com.profitsoft.xml.write.ItemHolder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The class that finds all files in a specified directory and
 * starts the json parsing.
 *
 * @param <T> a data type. Could be Integer or String, depends on the attribute.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileResolver<T> {

    StatsService<T> statsService;
    String folderName;
    String attributeName;
    ExecutorService executor;
    AtomicReference<Integer> analyzedFilesAmount;
    Integer THREADS_AMOUNT = 8;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public FileResolver(String folderName, String attributeName) {
        this.folderName = folderName;
        this.attributeName = attributeName;
        this.statsService = new StatsService<>();
        this.executor = Executors.newFixedThreadPool(THREADS_AMOUNT);
        this.analyzedFilesAmount = new AtomicReference<>();
    }

    /**
     * Finds all files in the user specified directory and starts
     * json parsing. It uses multithreading to improve the performance.
     */
    public void start() {
        try {
            File folder = new File(folderName);
            List<File> fileList;

            if (folder.isDirectory()) {
                fileList = Arrays.stream(folder.listFiles())
                        .filter(file -> file.isFile() && file.getName().endsWith(".json"))
                        .toList();
                analyzedFilesAmount.set(fileList.size());
            } else {
                logger.error("current folder name is not a directory");
                return;
            }

            Long parsingStart = System.currentTimeMillis();
            List<Callable<Object>> todo = new ArrayList<>(fileList.size());
            for (File file : fileList) {
                todo.add(Executors.callable(() -> resolveFile(file)));
            }

            executor.invokeAll(todo);

            Long parsingEnd = System.currentTimeMillis();
            executor.shutdown();
            logger.info("Amount of threads for parsing: " + THREADS_AMOUNT);
            logger.info("Time of parsing execution: " + ((parsingEnd - parsingStart) + " ms"));

            logger.info("Analyzed " + analyzedFilesAmount + " json files with correct form.");
            logger.info("General amount of files in directory: " + fileList.size());
        } catch (Exception e) {
            logger.error("error during the file resolving: " + e.getMessage());
        }
    }

    /**
     * Helper method that starts json parsing for a file.
     * After parsing method get result list and adds it to
     * the general stats' calculator. In case some exception
     * is raised, then the results of parsing won't be added to the general
     * statistics and amount of analysed files ('analyzedFilesAmount') will be decreased.
     *
     * @param file file to be parsed
     */
    private void resolveFile(File file) {
        try {
            JsonAnalyzer<T> jsonAnalyzer = new JsonAnalyzer<>(attributeName, file);
            List<T> attributeOccurrences = jsonAnalyzer.analyse();
            attributeOccurrences.forEach(statsService::addAttributeOccurrence);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            analyzedFilesAmount.getAndSet(analyzedFilesAmount.get() - 1);
            Thread.currentThread().interrupt();
        } finally {
        }
    }

    /**
     * Returns the result of stats calculation after all files were parsed.
     *
     * @return the result of stats calculation
     */
    public List<ItemHolder<T>> analysisResult() {
        return statsService.getStatsResult();
    }
}