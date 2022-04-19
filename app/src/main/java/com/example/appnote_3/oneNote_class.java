package com.example.appnote_3;

public class oneNote_class {
    private int id;
    private String textV_title, textV_content, textV_day, textV_time;
    private byte[] img_font;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public oneNote_class(int id,String textV_title, String textV_content, String textV_day, String textV_time, byte[] img_font) {
        this.id = id;
        this.textV_title = textV_title;
        this.textV_content = textV_content;
        this.textV_day = textV_day;
        this.textV_time = textV_time;
        this.img_font = img_font;
    }


    public String getTextV_title() {
        return textV_title;
    }

    public void setTextV_title(String textV_title) {
        this.textV_title = textV_title;
    }

    public String getTextV_content() {
        return textV_content;
    }

    public void setTextV_content(String textV_content) {
        this.textV_content = textV_content;
    }

    public String getTextV_time() {
        return textV_time;
    }

    public void setTextV_time(String textV_time) {
        this.textV_time = textV_time;
    }

    public String getTextV_day() {
        return textV_day;
    }

    public void setTextV_day(String textV_day) {
        this.textV_day = textV_day;
    }

    public byte[] getImg_font() {
        return img_font;
    }

    public void setImg_font(byte[] img_font) {
        this.img_font = img_font;
    }
}
