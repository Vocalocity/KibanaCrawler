package com.vonage.kibana_crawler.utilities.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Symbols {

    UNDERSCORE('_'),
    FORWARD_SLASH('/'),
    BACK_SLASH('\\'),
    COMMA(','),
    AMPERSAND('&'),
    OPEN_SQUARE_BRACKET('['),
    PIPE('|'),
    SPACE(' '),
    PERIOD('.'),
    CLOSED_SQUARE_BRACKET(']'),
    ASTERISK('*');

    private final char symbol;

    public String asString(){
        return String.valueOf(this.symbol);
    }
}
