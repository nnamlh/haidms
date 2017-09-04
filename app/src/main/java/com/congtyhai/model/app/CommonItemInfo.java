package com.congtyhai.model.app;

import java.util.Objects;

/**
 * Created by HAI on 9/4/2017.
 */

public class CommonItemInfo {
    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CommonItemInfo (String name, String code) {
        this.name = name;
        this.code = code;
    }

}
