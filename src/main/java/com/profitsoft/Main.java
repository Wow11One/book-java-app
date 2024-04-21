package com.profitsoft;

import com.profitsoft.json.parse.FileResolver;
import com.profitsoft.xml.write.XmlWriter;
import com.profitsoft.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Main class, where the program starts working.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * main method that is starting the program.
     *
     * @param args an array of args.
     *             First element of this array should be the name of a folder,
     *             from where json-data should be fetched.
     *             The second arg is an attribute-name, which defines by which
     *             attribute statistics should be made.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            logger.error("user should provide 2 parameters: folder-name and attribute-name");
            return;
        }
        try {
            String folderName = args[0];
            String attributeName = args[1];
            switch (attributeName) {
                case Constants.GENRE:
                case Constants.PUBLICATION_HOUSE:
                    handleUserAttribute(folderName, attributeName, String.class);
                    break;
                case Constants.YEAR_PUBLISHED:
                    handleUserAttribute(folderName, attributeName, Integer.class);
                    break;
                default:
                    logger.error("attribute is unknown");

            }
        } catch (Exception exception) {
            logger.error("unfortunately exception was raised:");
            logger.error(exception.getMessage());
        }
    }

    /**
     * Helper method that handles user input
     * and create required objects.
     *
     * @param folderName     a name of folder, where the program
     *                       should fetch json files. There is no need to specify
     *                       an absolute file path, because for the user convenience
     *                       all test data are stored in resources folder in the project.
     *                       Therefore, user need to write the name of the folder from
     *                       the resources' directory.
     * @param attributeName  the attribute name by which statistics
     *                       should be calculated. There are 3 possible options for this field:
     *                       <dl>
     *                       <dt>year-published: Integer</dt>
     *                       <dd>a year when book was published</dd>
     *                       <dt>publication_house: String</dt>
     *                       <dd>a publication house of this book</dd>
     *                       <dt>genre: String</dt>
     *                       <dd>stores info about genres. This field can have multiple values,
     *                       separated by commas
     *                        For instance: "Fiction, Comedy, Tragedy"
     *                       </dd>
     *                       <dl/>
     * @param classReference the type of attribute that should be handled.
     *                       Could be string or integer.
     * @param <T>            the type of attribute that should be handled.
     *                       Could be string or integer.
     * @throws IOException in case XmlWriter class instance can't find the
     *                      specified file.
     */
    private static <T> void handleUserAttribute(String folderName,
                                                String attributeName,
                                                Class<T> classReference)
            throws IOException {
        String resultFolderName = Constants.PROJECT_PATH + "/json/" + folderName;
        FileResolver<T> fileResolver = new FileResolver<>(resultFolderName, attributeName);
        fileResolver.start();

        XmlWriter<T> xmlWriter = new XmlWriter<>();
        xmlWriter.writeValueAsXml(fileResolver.analysisResult(), attributeName, folderName);
    }
}