
package com.xbw.youdao;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("us-phonetic")
    @Expose
    private String usPhonetic;
    @SerializedName("phonetic")
    @Expose
    private String phonetic;
    @SerializedName("uk-phonetic")
    @Expose
    private String ukPhonetic;
    @SerializedName("explains")
    @Expose
    private List<String> explains = null;

    public String getUsPhonetic() {
        return usPhonetic;
    }

    public void setUsPhonetic(String usPhonetic) {
        this.usPhonetic = usPhonetic;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getUkPhonetic() {
        return ukPhonetic;
    }

    public void setUkPhonetic(String ukPhonetic) {
        this.ukPhonetic = ukPhonetic;
    }

    public List<String> getExplains() {
        return explains;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }

}
