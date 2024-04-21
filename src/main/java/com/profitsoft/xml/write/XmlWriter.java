package com.profitsoft.xml.write;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.profitsoft.Main;
import com.profitsoft.utils.Constants;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The class that provides marshalling for a list of ItemHolder class
 * instances.
 *
 * @param <T> a data type. Could be Integer or String, depends on attribute
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class XmlWriter<T> {

    XmlMapper xmlMapper;
    String RESULT_FOLDER_NAME;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public XmlWriter() {
        this.xmlMapper = new XmlMapper();
        this.RESULT_FOLDER_NAME = "xml-results";
    }

    /**
     * creates or changes an existing file and fills it with
     * the result of statistics calculation in xml format.
     * It saves the results in 'resources/xml-results' directory
     * and creates subdirectory with the same name as the one that
     * user specified. For example: as startup args user provided 'task-example'
     * for the folder name and 'genre' - for the attribute name, then
     * new xml file will have an address:'resources/xml-results/task-example/statistics_by_genre.xml'
     *
     * @param resultList     the result of attribute occurrence in certain files.
     * @param attribute      the attribute name to specify xml file name
     * @param userFolderName the name of a folder based on which user chooses to make stats
     * @throws IOException   in case XmlWriter can't save the file
     */
    public void writeValueAsXml(List<ItemHolder<T>> resultList, String attribute, String userFolderName)
            throws IOException {
        ResultMapHolder<T> resultMapHolder = new ResultMapHolder<>(resultList);
        if (!resultMapHolder.getItem().isEmpty()) {
            String resultFolderName = Constants.PROJECT_PATH + RESULT_FOLDER_NAME + "/" + userFolderName;
            File dir = new File(resultFolderName);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            String resultFilePath = resultFolderName + "/statistics_by_" + attribute + ".xml";

            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.writeValue(new File(resultFilePath), resultMapHolder);

            logger.info("Results are successfully saved");
        } else {
            throw new IllegalArgumentException("The object needed to save is empty");
        }
    }
}
