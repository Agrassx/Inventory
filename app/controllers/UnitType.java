package controllers;

public class UnitType {

    private int id;
    private String nameOfType;

    public UnitType(int id, String type) {
        this.id = id;
        this.nameOfType = type;
    }

    public UnitType() {

    }

    public int getId() {
        return id;
    }

    public String getNameOfType() {
        return nameOfType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNameOfType(String nameOfType) {
        this.nameOfType = nameOfType;
    }
}
