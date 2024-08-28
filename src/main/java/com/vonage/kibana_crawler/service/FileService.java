package com.vonage.kibana_crawler.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Objects;
import java.util.Scanner;

@Component
@Slf4j
@AllArgsConstructor
public class FileService {

    public synchronized void writeResult(File file, boolean deleteFileIfExist, String... data) {
        if(deleteFileIfExist){
            deleteFileIfExists(file);
        }
        createFileIfNotExists(file);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getName(), true))) {
            for (String dataItem : data) {
                writer.append(dataItem).append("\n");
            }
        } catch (Exception e) {
            log.error("Error writing file {}", file.getName(), e);
        }
    }

    public String readFrom(File file) {
        if(Objects.isNull(file) || !file.exists()){
            return "";
        }
        StringBuilder contentBuilder = new StringBuilder();
        try(Scanner reader = new Scanner(file)){
            while (reader.hasNextLine()) {
                contentBuilder.append(reader.nextLine()).append("\n");
            }
            return contentBuilder.toString();
        } catch (FileNotFoundException e) {
            log.error("Unable to read file {}. ERROR: {}", file.getAbsoluteFile(), e.getMessage());
        }
        return "";
    }

    public void createFileIfNotExists(File file) {
        try {
            if (!file.exists()) {
                log.info("Creating new file {}, Result {}", file.getAbsolutePath(), file.createNewFile());
            }
        } catch (Exception e) {
            log.error("Unable to create file {}. ERROR: {}", file.getAbsoluteFile(), e.getMessage());
        }
    }

    public void deleteFileIfExists(File file) {
        try {
            if (file.exists()) {
                log.info("Deleting file {} status {}", file.getAbsoluteFile(), file.delete());
            }
        } catch (Exception e) {
            log.error("Unable to delete file {}. ERROR: {}", file.getAbsoluteFile(), e.getMessage());
        }
    }

    public void deleteFileIfExists(String filename) {
        deleteFileIfExists(new File(filename));
    }
}