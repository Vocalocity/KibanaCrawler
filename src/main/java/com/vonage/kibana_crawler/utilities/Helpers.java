package com.vonage.kibana_crawler.utilities;

import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.WorksheetCopyType;
import com.vonage.kibana_crawler.utilities.constants.FileTypes;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public final class Helpers {

    private Helpers() {}

    /**
     *
     * @param a first frequency map, also the resultant map
     * @param b second frequency map </br>
     * @apiNote If any param is null, method will throw null pointer exception.
     */
    public static <T, V extends Number> void mergeFrequencyMaps(Map<T, V> a, Map<T, V> b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
//        b.forEach((k, v) -> a.merge(k, v, Helpers::add));
    }

    public static Number add(Number v1, Number v2) {
        Objects.requireNonNull(v1);
        Objects.requireNonNull(v2);
        if(v1 instanceof Integer) {
            return v1.intValue() + v2.intValue();
        }
        if(v1 instanceof Long) {
            return v1.longValue() + v2.longValue();
        }
        if(v1 instanceof Double) {
            return v1.doubleValue() + v2.doubleValue();
        }
        if(v1 instanceof Float) {
            return v1.floatValue() + v2.floatValue();
        }
        return null;
    }

    public static void shutdown(ExecutorService executorService, long timeout, TimeUnit timeUnit){
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(timeout, timeUnit)) {
                executorService.shutdownNow();
            }
        }catch (Exception e) {
            executorService.shutdownNow();
        }
    }

    public static <T> Stream<List<T>> batches(List<T> source, int length) {
        if (length <= 0)
            throw new IllegalArgumentException("length = " + length);
        int size = source.size();
        if (size == 0)
            return Stream.empty();
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }

    /* Will return false if file is unreadable. */
    public static boolean validateFile(File file, FileTypes expectedType){
        return file.exists() && file.isFile() && file.canRead() && file.getName().endsWith(expectedType.getExtension());
    }

    public static void mergeSheets(File excelFiles, String masterExcelName) {
        Workbook master = new Workbook(), temp = new Workbook();
        Arrays.stream(Objects.requireNonNull(excelFiles.listFiles())).forEach(file -> {
            if (Helpers.validateFile(file, FileTypes.XLSX)) {
                log.info(file.getAbsolutePath());
                temp.loadFromFile(file.getAbsolutePath());
                master.getWorksheets().addCopy(temp.getWorksheets().get(0), WorksheetCopyType.CopyAll);
            }
        });
        master.saveToFile(masterExcelName + FileTypes.XLSX.getExtension(), ExcelVersion.Version2016);
    }

    public static String shortDate(String date){
        return LocalDateTime
                .parse(
                        date.replace( " " , "T" )
                )
                .toLocalDate()
                .format (
                        DateTimeFormatter
                                .ofLocalizedDate( FormatStyle.SHORT )
                                .withLocale( Locale.FRANCE )
                ).replace("/", "-");
    }
}
