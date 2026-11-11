package uoc.ds.pr.util;

import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import org.apache.commons.csv.*;
import uoc.ds.pr.pr2.Browser;
import uoc.ds.pr.exceptions.DSException;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class CSVUtil {


    @FunctionalInterface
    public interface DSBiConsumer<T, U> {
        void accept(T t, U u) throws DSException;
    }

    public static CSVParser getCVSParser(String filePath) {

        CSVParser csvParser = null;
        try {
//            System.out.println(filePath);
            InputStream inputStream = CSVUtil.class.getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }

            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvParser;
    }

    public static void processRows(String csvFile, String[] fields,
                                    Browser browser,
                                   DSBiConsumer<Browser, CSVRecord > addFunction) throws DSException {
        CSVParser csvParser = getCVSParser(csvFile);
        for (CSVRecord record : csvParser) {
            addFunction.accept(browser, record);
        }
    }

    /**
     * Generic method to add data from a CSV source.
     * Uses BiConsumer to map an array of strings to the target method.
     */
    public static void addDataSource(
            String fileName,
            String[] headers,
            Browser browser,
            DSBiConsumer<Browser, String[]> methodMapping
    ) throws DSException {
        processRows(fileName, headers, browser, (si, record) -> {
            String[] values = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                values[i] = record.get(headers[i]);
            }
            methodMapping.accept(si, values);
        });
    }

}

