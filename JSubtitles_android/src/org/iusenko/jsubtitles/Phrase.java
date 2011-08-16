package org.iusenko.jsubtitles;

import java.io.Serializable;

public class Phrase implements Serializable {

    private String text;
    private String fromTime;
    private String toTime;

    public Phrase(String text) {
        this.text = text;
        this.fromTime = "00:00:00.00";
        this.toTime = "00:00:00.00";
    }

    public Phrase() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }
}
