package com.profitsoft;

import com.profitsoft.io.FileResolver;
import com.profitsoft.io.XmlWriter;
import com.profitsoft.utils.Constants;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            String folderName = Constants.PROJECT_PATH + "/json/" + args[0];
            String attributeName = args[1];
            switch (attributeName) {
                case Constants.GENRE:
                case Constants.PUBLICATION_HOUSE:
                    FileResolver<String> fileResolver = new FileResolver<>(folderName, attributeName);
                    fileResolver.start();
                    System.out.println(fileResolver.analysisResult());
                    XmlWriter<String> xmlWriter = new XmlWriter<>();
                    xmlWriter.toXml(fileResolver.analysisResult(), attributeName, folderName);
                    break;
                case Constants.YEAR_PUBLISHED:
                    fileResolver = new FileResolver<>(folderName, attributeName);
                    fileResolver.start();
                    System.out.println(fileResolver.analysisResult());
                    xmlWriter = new XmlWriter<>();
                    xmlWriter.toXml(fileResolver.analysisResult(), attributeName, folderName);

            }
        } catch (Exception exception) {
            System.out.println("unfortunately exception was raised:");
            System.out.println(exception.getMessage());
        }
    }
}