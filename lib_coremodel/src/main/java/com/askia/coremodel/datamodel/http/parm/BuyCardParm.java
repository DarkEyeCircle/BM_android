package com.askia.coremodel.datamodel.http.parm;

public class BuyCardParm {

    /*{
        "cardId":"",
        "securityPassword":"",
    }*/

    private String cardId;

    private String securityPassword;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getSecurityPassword() {
        return securityPassword;
    }

    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
    }
}
