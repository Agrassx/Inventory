package controllers;

public class UnitProp {
    private int id;
    private String name;
    private String dataType;
    private String value;

    public UnitProp() {

    }

    public UnitProp(Integer id, String name, String dataType) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
    }

    public UnitProp(String id, String value, String dataType) {
        this.id = Integer.parseInt(id);
        this.value = value;
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
