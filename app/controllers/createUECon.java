package controllers;

import play.db.DB;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.createUE;
import views.html.general;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class createUECon extends Controller {

    private static final int REL_RULES_CREATE = 1;

    public Result begin() {
        return ok(createUE.render("Choose type of unit", getUnitType(), getUnitProp()));
    }

    public Result addNewUnit() throws SQLException {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());
        List<UnitProp> unitProps = new ArrayList<>();
        InventoryUnit inventoryUnit = new InventoryUnit();
        Http.RequestBody body = request().body();
        Map<String, String[]> request = body.asFormUrlEncoded();

        for (int i = 1; i < 9; i++) {
            String prop = request.get("prop"+String.valueOf(i))[0];
            String value = request.get("value"+String.valueOf(i))[0];
            String propDT = request.get("propDT"+String.valueOf(i))[0];
            propDT = propDT.equals("0") ? "" : propDT;
            if(!prop.equals("0") && !value.equals("")) {
                unitProps.add(new UnitProp(prop, value, propDT));
            }
        }
        try {
            sqlInsertChangeLog(Integer.parseInt(session().get("UserId")), sqlDate);

            Integer ueType = Integer.parseInt(request.get("type")[0]);
            String invNum = request.get("invNum")[0];

            inventoryUnit.setInvNumber(invNum);
            inventoryUnit.setChangeId(sqlGetLast("id_change", "UE_CHANGE_LOG"));
            inventoryUnit.setIdType(ueType);

            sqlInsertUE(inventoryUnit);
            inventoryUnit.setId(sqlGetLast("id_ue", "UE"));

            sqlInsertUEpropVals(unitProps, inventoryUnit);
            sqlInsertUErels(inventoryUnit, REL_RULES_CREATE);
            return ok(general.render("UE successful created!"));

        } catch (SQLException e) {

            return badRequest(e.getMessage());

        }
    }

    private void sqlInsertUErels(InventoryUnit unit, int rule) throws SQLException {
        String sqlINSERTueRels = "INSERT INTO invetory.UE_RELS (id_change, id_ue_from, id_ue_to, id_rule) " +
                "VALUES (?, ?, ?, ?);";
        Connection connection = DB.getConnection("invetory", true);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlINSERTueRels);
        preparedStatement.setInt(1, unit.getChangeId());
        preparedStatement.setInt(2, unit.getId());
        preparedStatement.setInt(3, unit.getId());
        preparedStatement.setInt(4, rule);
        preparedStatement.executeUpdate();
        connection.close();
    }

    private void sqlInsertUEpropVals(List<UnitProp> unitProps, InventoryUnit inventoryUnit) throws SQLException {
        String sqlISERTUEPropVals = "INSERT INTO invetory.UE_PROPS_VALS (id_prop, id_ue, id_change, prop_ue_value) " +
                "VALUES (?, ?, ?, ?);";
        Connection connection = DB.getConnection("invetory", true);
        for (UnitProp p : unitProps) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlISERTUEPropVals);
            preparedStatement.setInt(1, p.getId());
            preparedStatement.setInt(2, inventoryUnit.getId());
            preparedStatement.setInt(3, inventoryUnit.getChangeId());
            preparedStatement.setString(4, p.getValue());
            preparedStatement.executeUpdate();
        }
        connection.close();

    }

    private void sqlInsertChangeLog(int userId, Date date) throws SQLException {
        String sqlINSERTchangeLog = "INSERT INTO invetory.UE_CHANGE_LOG (id_change, id_user, mod_date) " +
                "VALUES (NULL, ?, ?)";
        Connection connection = DB.getConnection("invetory", true);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlINSERTchangeLog);
        preparedStatement.setInt(1, userId);
        preparedStatement.setDate(2, date);
        preparedStatement.executeUpdate();
        connection.close();
    }

    private void sqlInsertUE(InventoryUnit inventoryUnit) throws SQLException {
        String sqlINSERTue = "INSERT INTO invetory.UE (id_ue, id_type, id_change, ue_inv_num) " +
                "VALUES (NULL, ?, ?, ?)";
        Connection connection = DB.getConnection("invetory", true);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlINSERTue);
        preparedStatement.setInt(1, inventoryUnit.getIdType());
        preparedStatement.setInt(2, inventoryUnit.getChangeId());
        preparedStatement.setString(3, inventoryUnit.getInvNumber());
        preparedStatement.executeUpdate();
        connection.close();
    }

    private Integer sqlGetLast(String column, String table) throws SQLException {
        String sqlSelectMax = String.format("SELECT MAX(%s) FROM invetory.%s", column, table);
        Connection connection = DB.getConnection("invetory", true);
        Integer result = 0;
        ResultSet resultSet = connection.createStatement().executeQuery(sqlSelectMax);
        while (resultSet.next()) {
            result = resultSet.getInt(String.format("MAX(%S)",column));
        }
        return result;
    }

    private List<UnitType> getUnitType(){
        String error = "";
        String sqlQuery = "SELECT * FROM UE_TYPE ORDER BY id_type ASC";
        List<UnitType> result = new ArrayList<>();
        try {
            Connection connection = DB.getConnection("invetory", true);
            ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery();
            while (resultSet.next()) {
                result.add(new UnitType(resultSet.getInt("id_type"), resultSet.getString("type_name")));
            }
            resultSet.close();
            connection.close();

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            error += e.getMessage();
            return result;
        }
    }

    private List<UnitProp> getUnitProp() {
        List<UnitProp> result = new ArrayList<>();
        String sqlQuery = "SELECT * FROM UE_PROPERTIES";
        String error = "";
        try {
            Connection connection = DB.getConnection("invetory", true);
            ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery();
            while (resultSet.next()) {
                result.add(new UnitProp(resultSet.getInt("id_prop"), resultSet.getString("prop_name"),
                        resultSet.getString("prop_data_type")));
            }
            resultSet.close();
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            error += e.getMessage();
            return result;
        }
    }

}
