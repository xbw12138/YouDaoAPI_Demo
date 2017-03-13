
package com.xbw.youdao.gson;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Web {

    @SerializedName("value")
    @Expose
    private List<String> value = null;
    @SerializedName("key")
    @Expose
    private String key;

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
