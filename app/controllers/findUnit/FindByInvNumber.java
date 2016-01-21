package controllers.findUnit;

public class FindByInvNumber {
    private String invNum;
    private String typeName;
    private String propName;
    private String propUeValue;
    private String propDataType;
    private String nameRule;
    private String userName;
    private String userSurname;
    private String modDate;

    public FindByInvNumber(String invNum, String typeName, String propName, String propUeValue, String propDataType,
                           String nameRule, String userName, String userSurname, String modDate) {
        this.invNum = invNum;
        this.typeName = typeName;
        this.propName = propName;
        this.propUeValue = propUeValue;
        this.propDataType = propDataType;
        this.nameRule = nameRule;
        this.userName = userName;
        this.userSurname = userSurname;
        this.modDate = modDate;

    }

    public String getInvNum() {
        return invNum;
    }

    public String getModDate() {
        return modDate;
    }

    public String getNameRule() {
        return nameRule;
    }

    public String getPropDataType() {
        return propDataType;
    }

    public String getPropName() {
        return propName;
    }

    public String getPropUeValue() {
        return propUeValue;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public void setNameRule(String nameRule) {
        this.nameRule = nameRule;
    }

    public void setPropDataType(String propDataType) {
        this.propDataType = propDataType;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public void setPropUeValue(String propUeValue) {
        this.propUeValue = propUeValue;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }
}
