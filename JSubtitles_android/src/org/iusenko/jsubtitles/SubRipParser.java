package org.iusenko.jsubtitles;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class SubRipParser {

    private static final Pattern phraseNumberPattern = Pattern.compile("\\d+");
    private static final Pattern timePattern = Pattern.compile("\\d+(:\\d+)+,\\d+\\s+-->\\s+\\d+(:\\d+)+,\\d+");
    private LinkedList<Phrase> phrases;
    private InputStream in;

    public SubRipParser(InputStream in) {
        this.in = in;
    }

    public LinkedList<Phrase> getPhrases() throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        phrases = new LinkedList<Phrase>();
        String line;
        StringBuilder buffer = new StringBuilder();
        Phrase phrase = null;

        while ((line = bf.readLine()) != null) {
            if (isBlank(line)) {
                phrase.setText(buffer.toString());
                phrases.add(phrase);
                continue;
            }

            if (isTime(line)) {
                phrase.setFromTime(getStartTime(line));
                phrase.setToTime(getEndTime(line));
                continue;
            }

            if (isPhraseNumber(line)) {
                phrase = new Phrase();
                buffer = new StringBuilder();
                continue;
            }

            if (isSentence(line)) {
                buffer.append(" ").append(line);
            }

        }
        bf.close();
        return phrases;
    }

    boolean isSentence(String line) {
        if (isBlank(line) || isTime(line) || isPhraseNumber(line)) {
            return false;
        }
        return true;
    }

    boolean isTime(String line) {
        return timePattern.matcher(line).matches();
    }

    boolean isPhraseNumber(String line) {
        return phraseNumberPattern.matcher(line).matches();
    }

    protected String getStartTime(String rawString) {
        return getTime(rawString, true);
    }

    protected String getEndTime(String rawString) {
        return getTime(rawString, false);
    }

    protected String getTime(String rawString, boolean getStartTime) {
        int index = rawString.indexOf("-->");
        if (index == -1) {
            return "";
        }

        rawString = rawString.replaceAll(",", ".");
        if (getStartTime) {
            return rawString.substring(0, index).trim();
        } else {
            return rawString.substring(index + 3).trim();
        }
    }

    private boolean isBlank(String string) {
        return string == null || "".equals(string.trim());
    }

}
