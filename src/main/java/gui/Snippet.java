package gui;

import java.io.Serializable;

public class Snippet implements Serializable {
    private String keywords;
    private String language;
    private String name;
    private String code;

    public Snippet(String keywords, String language, String name, String code) {
        this.keywords = keywords;
        this.language = language;
        this.name = name;
        this.code = code;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

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
}
