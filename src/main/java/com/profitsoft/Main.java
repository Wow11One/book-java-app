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

                    XmlWriter<String> xmlWriter = new XmlWriter<>();
                    xmlWriter.toXml(fileResolver.analysisResult(), attributeName, args[0]);
                    break;
                case Constants.YEAR_PUBLISHED:
                    FileResolver<String> fileResolverInteger = new FileResolver<>(folderName, attributeName);
                    fileResolverInteger.start();

                    xmlWriter = new XmlWriter<>();
                    xmlWriter.toXml(fileResolverInteger.analysisResult(), attributeName, args[0]);
                    break;
                default: System.out.println("attribute is unknown");

            }
        } catch (Exception exception) {
            System.out.println("unfortunately exception was raised:");
            System.out.println(exception.getMessage());
        }
    }
}