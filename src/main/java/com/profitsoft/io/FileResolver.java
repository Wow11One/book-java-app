package com.profitsoft.io;


import com.profitsoft.service.StatsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileResolver<T> {

    final StatsService<T> statsService;
    final String folderName;
    final String attributeName;
    final ExecutorService executor;
    final AtomicReference<Integer> analyzedFilesAmount;

    public FileResolver(String folderName, String attributeName) {
        this.statsService = new StatsService<>();
        this.folderName = folderName;
        this.attributeName = attributeName;
        this.executor = Executors.newFixedThreadPool(2);
        this.analyzedFilesAmount = new AtomicReference<>();
    }

    public void start() {
        try {
            File folder = new File(folderName);
            System.out.println(folder.getAbsolutePath());
            List<File> fileList;

            if (folder.isDirectory()) {
                fileList = Arrays.stream(folder.listFiles())
                        .filter(file -> file.isFile() && file.getName().endsWith(".json"))
                        .toList();
                analyzedFilesAmount.set(fileList.size());
            } else {
                System.out.println("current folder name is not a directory");
                return;
            }
            List<Future<?>> futures = new ArrayList<>();

            for (final File file : fileList) {
                Future<?> future = executor.submit(() -> resolveFile(file));
                futures.add(future);
            }

            for (Future<?> future : futures) {
                future.get();
            }
            executor.shutdown();
            System.out.println("Analyzed " + analyzedFilesAmount + " json files with correct form.");
            System.out.println("General amount of files in directory: " + fileList.size());
        } catch (
                Exception e) {
            System.out.println("file does not exist");
        }
    }

    private void resolveFile(File file) {
        try {
            JsonAnalyzer<T> jsonAnalyzer = new JsonAnalyzer<>(attributeName, file);
            List<T> attributeOccurrences = jsonAnalyzer.analyse();
            attributeOccurrences.forEach(statsService::addAttributeOccurrence);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            analyzedFilesAmount.getAndSet(analyzedFilesAmount.get() - 1);
            Thread.currentThread().interrupt();
        }
    }

    public Map<T, Integer> analysisResult() {
        return statsService.getAttributeStats().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, ConcurrentHashMap::new));
    }
}