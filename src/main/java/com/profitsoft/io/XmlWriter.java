package com.profitsoft.io;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.profitsoft.utils.Constants;
import com.profitsoft.utils.ResultMapHolder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class XmlWriter<T> {

    XmlMapper xmlMapper;
    String RESULT_FOLDER_NAME = "xml-results";

    public XmlWriter() {
        this.xmlMapper = new XmlMapper();
    }

    public void toXml(Map<T, Integer> resultMap, String attribute, String userFolderName) throws IOException {
        ResultMapHolder<T> resultMapHolder = new ResultMapHolder<>(resultMap);
        if (!resultMapHolder.getItem().isEmpty()) {
            File dir = new File(Constants.PROJECT_PATH + RESULT_FOLDER_NAME + userFolderName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String resultFilePath = Constants.PROJECT_PATH + RESULT_FOLDER_NAME + "/"
                    + userFolderName + "/statistics_by_" + attribute + ".xml";

            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.writeValue(new File(resultFilePath), resultMapHolder);

            System.out.println("Results are successfully saved");
        } else {
            System.out.println("Results are successfully saved");
        }
    }
}
