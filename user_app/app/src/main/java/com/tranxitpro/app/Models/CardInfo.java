package com.tranxitpro.app.Models;

/**
 * Created by amal on 22/02/17.
 */
public class CardInfo {
    private String lastFour;
    private String cardId;
    private String cardType;

    public String getLastFour() {
        return lastFour;
    }

    public void setLastFour(String lastFour) {
        this.lastFour = lastFour;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
