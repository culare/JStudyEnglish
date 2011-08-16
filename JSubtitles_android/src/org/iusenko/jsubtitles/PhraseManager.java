package org.iusenko.jsubtitles;

import java.io.Serializable;
import java.util.LinkedList;

public class PhraseManager implements Serializable {

    public static final Phrase END = new Phrase("End");
    public static final Phrase START = new Phrase("Start");
    private LinkedList<Phrase> phrases;
    private int index;

    public void setPhrases(LinkedList<Phrase> subtitles) {
        if (isEmpty(subtitles)) {
            throw new IllegalArgumentException("Collection of phrases can't be empty");
        }
        this.phrases = subtitles;
        index = 0;
    }

    public Phrase next() {
        if (phrases == null || (index + 1) >= phrases.size()) {
            return END;
        }
        index++;
        return phrases.get(index);
    }

    public Phrase prev() {
        if (phrases == null || (index - 1) <= 0) {
            return START;
        }
        index--;
        return phrases.get(index);
    }

    private boolean isEmpty(LinkedList collection) {
        return collection == null || collection.size() == 0;
    }

    public Phrase current() {
        if (phrases == null && index >= phrases.size()) {
            return START;
        }
        return phrases.get(index);
    }
}
