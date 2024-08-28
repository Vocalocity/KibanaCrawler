package com.vonage.kibana_crawler.utilities.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileTypes {
    TXT("txt"),
    CSV("csv"),
    XLSX("xlsx"),
    JSON("json");

    private final String name;

    public String getExtension(){
        return Symbols.PERIOD.getSymbol() + name;
    }
}
