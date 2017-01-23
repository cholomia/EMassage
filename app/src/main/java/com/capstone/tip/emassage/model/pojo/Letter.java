package com.capstone.tip.emassage.model.pojo;

/**
 * @author pocholomia
 * @since 23/01/2017
 */

public class Letter {

    private String letter;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean isEmpty() {
        String ltr = letter.replaceAll("\\s+", "");
        return ltr.isEmpty();
    }

}
