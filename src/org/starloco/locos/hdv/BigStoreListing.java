package org.starloco.locos.hdv;

import org.starloco.locos.object.GameObject;

public class BigStoreListing implements Comparable<BigStoreListing> {
    private int id;
    private int hdvId;
    private int lineId;
    private final int owner;
    private final int price;
    private final byte amount;            //Dans le format : 1=1 2=10 3=100
    private final GameObject gameObject;

    public BigStoreListing(int price, byte amount, int owner, GameObject gameObject) {
        this(-1, price, amount, owner, gameObject);
    }

    public BigStoreListing(int id, int price, byte amount, int owner, GameObject gameObject) {
        this.id = id;
        this.price = price;
        this.amount = amount;
        this.gameObject = gameObject;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHdvId() {
        return this.hdvId;
    }

    public void setHdvId(int id) {
        this.hdvId = id;
    }

    void setLineId(int lineId) {
        this.lineId = lineId;
    }
    public int getLineId() {
        return lineId;
    }

    public int getOwner() {
        return this.owner;
    }

    public int getPrice() {
        return this.price;
    }

    public byte getAmount() {
        return this.amount;
    }

    public byte getAmountExp() {
        return (byte)Math.pow(10, amount);
    }

    public GameObject getGameObject() {
        return this.gameObject;
    }

    public String parseToEL() {
        // For EL packet, we want to be able to identify each listing, so we return the listing ID
        StringBuilder toReturn = new StringBuilder();
        int count = getAmountExp();//Transf�re dans le format (1,10,100) le montant qui etait dans le format (0,1,2)
        toReturn.append(this.getId()).append(";").append(count).append(";").append(this.getGameObject().getTemplate().getId()).append(";").append(this.getGameObject().parseStatsString()).append(";").append(this.price).append(";350");//350 = temps restant
        return toReturn.toString();
    }

    public String parseToEmK() {
        StringBuilder toReturn = new StringBuilder();
        int count = getAmountExp();//Transf�re dans le format (1,10,100) le montant qui etait dans le format (0,1,2)
        toReturn.append(this.getGameObject().getGuid()).append("|").append(count).append("|").append(this.getGameObject().getTemplate().getId()).append("|").append(this.getGameObject().parseStatsString()).append("|").append(this.price).append("|350");//350 = temps restant
        return toReturn.toString();
    }

    public int compareTo(BigStoreListing o) {
        BigStoreListing e = o;
        int celuiCi = this.getPrice();
        int autre = e.getPrice();
        if (autre > celuiCi)
            return -1;
        if (autre == celuiCi)
            return 0;
        if (autre < celuiCi)
            return 1;
        return 0;
    }
}