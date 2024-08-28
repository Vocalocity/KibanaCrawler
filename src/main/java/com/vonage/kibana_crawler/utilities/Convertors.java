package com.vonage.kibana_crawler.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spire.xls.*;
import com.vonage.kibana_crawler.utilities.constants.CrawlerConstants;
import com.vonage.kibana_crawler.utilities.constants.FileTypes;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.EnumSet;
import java.util.Map;

@Slf4j
public final class Convertors {

    private Convertors() {}

    public static String mapToCSV(Map<?, ?> map){
        StringBuilder csvBuilder = new StringBuilder();
        map.forEach((key, value) -> csvBuilder
                .append(key.toString()).append(",")
                .append(value.toString())
                .append("\n"));
        return csvBuilder.toString();
    }

    public static String mapToJSON(Map<?, ?> map){
        try {
            return CrawlerConstants.MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
           log.error("Unable to read map. Error: {}", e.getMessage());
        }
        return null;
    }

    public static void convertCSVToExcel(File csvFile){
        if(!Helpers.validateFile(csvFile, FileTypes.CSV)){
            throw new IllegalArgumentException("CSV file does not exist or is not a CSV file.");
        }
        try{
            Workbook workbook = new Workbook();
            workbook.loadFromFile(csvFile.getAbsolutePath(), ",");

            for (int i = 0; i < workbook.getWorksheets().getCount(); i++)
            {
                Worksheet sheet = workbook.getWorksheets().get(i);
                CellRange usedRange = sheet.getAllocatedRange();
                usedRange.setIgnoreErrorOptions(EnumSet.of(IgnoreErrorType.NumberAsText));
                usedRange.autoFitColumns();
                usedRange.autoFitRows();
            }
            workbook.saveToFile(csvFile.getName().replace(FileTypes.CSV.getExtension(), FileTypes.XLSX.getExtension()), ExcelVersion.Version2016);

        } catch (Exception e){
            log.error("Unable to read file {}.Error {}", csvFile.getName(), e.getMessage());
        }
    }
}
