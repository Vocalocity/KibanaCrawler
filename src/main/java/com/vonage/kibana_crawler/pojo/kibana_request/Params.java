package com.vonage.kibana_crawler.pojo.kibana_request;

import com.vonage.kibana_crawler.utilities.constants.Symbols;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;

@Data
@NoArgsConstructor
public class Params {

    private String index;

    private Body body;

    private Long preference;

    public String getIndex() {
        if (StringUtils.isEmpty(index)) {
            throw new InvalidParameterException("'index' param is required.");
        }
        return index;
    }

    public void setIndex(String index) {
        if(!index.endsWith(Symbols.ASTERISK.asString())){
            index = index + Symbols.ASTERISK.asString();
        }
        this.index = index;
    }
}
