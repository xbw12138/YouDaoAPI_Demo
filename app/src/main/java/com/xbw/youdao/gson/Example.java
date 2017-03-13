
package com.xbw.youdao.gson;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("translation")
    @Expose
    private List<String> translation = null;
    @SerializedName("basic")
    @Expose
    private Basic basic;
    @SerializedName("query")
    @Expose
    private String query;
    @SerializedName("errorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("web")
    @Expose
    private List<Web> web = null;

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public List<Web> getWeb() {
        return web;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }

}
