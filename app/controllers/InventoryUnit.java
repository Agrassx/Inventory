package controllers;

public class InventoryUnit {

    private int id;
    private int idType;
    private String invNumber;
    private int changeId;

    public InventoryUnit() {

    }

    public InventoryUnit(int id, String invNumber, int idType, int changeId) {
        this.id = id;
        this.invNumber = invNumber;
        this.idType = idType;
        this.changeId = changeId;
    }

    public int getIdType() {
        return idType;
    }

    public int getChangeId() {
        return changeId;
    }

    public int getId() {
        return id;
    }

    public String getInvNumber() {
        return invNumber;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public void setChangeId(int changeId) {
        this.changeId = changeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }
}
