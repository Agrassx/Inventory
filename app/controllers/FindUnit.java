package controllers;

import controllers.findUnit.FindByInvNumber;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.find;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindUnit  extends Controller {

    private static final int FIND_BY_INV_NUMBER = 1;
    private static final int FIND_BY_TYPE = 2;
    private static final int FIND_BY_PROP = 3;
    private static final int FIND_BY_CHANGER = 4;

    public Result find() {
        return ok(find.render("Here you can find something", null));
    }

    public Result findIt() {
        Http.RequestBody body = request().body();
        Map<String, String[]> request = body.asFormUrlEncoded();
        Integer findBy = Integer.parseInt(request.get("findBy")[0]);
        String value = request.get("invNum")[0];
        List<FindByInvNumber> result;
        try {
            switch (findBy) {
                case 1: result = findByInvNumber(FIND_BY_INV_NUMBER, value); break;
                case 2: result = findByInvNumber(FIND_BY_TYPE, value); break;
                case 3: result = findByInvNumber(FIND_BY_PROP, value); break;
                case 4: result = findByInvNumber(FIND_BY_CHANGER, value); break;
                    default: result = findByInvNumber(FIND_BY_INV_NUMBER, value);
            }
            if (result.isEmpty()) {
                return ok(find.apply("Not founded!"+findBy.toString(), null));
            } else {
                return ok(find.render("Was founded: ", result));
            }
        } catch (SQLException e) {
            return badRequest(e.getMessage());
        }
    }

    private List<FindByInvNumber> findByInvNumber(int findBy, String value) throws SQLException {
        String column;
        switch (findBy) {
            case 2: column = "UE_TYPE.type_name"; break;
            case 1: column = "UE.ue_inv_num"; break;
            case 3: column = "UE_PROPERTIES.prop_name"; break;
            case 4: column = "UE_USERS.user_surname"; break;
                default: column = "UE.ue_inv_num";
        }
        String sqlSelect = String.format("SELECT UE.ue_inv_num, UE_TYPE.type_name, UE_PROPERTIES.prop_name, UE_PROPS_VALS.prop_ue_value, UE_PROPERTIES.prop_data_type,\n" +
                "\t\tUE_REL_RULES.name_rule, UE_USERS.user_name, UE_USERS.user_surname, UE_CHANGE_LOG.mod_date FROM UE \n" +
                "\t\t\tJOIN UE_PROPS_VALS ON UE.id_ue = UE_PROPS_VALS.id_ue\n" +
                "    \tJOIN UE_PROPERTIES ON UE_PROPS_VALS.id_prop = UE_PROPERTIES.id_prop\n" +
                "    \tJOIN UE_TYPE ON UE.id_type = UE_TYPE.id_type\n" +
                "    \tJOIN UE_RELS ON UE.id_ue = UE_RELS.id_ue_to\n" +
                "    \tJOIN UE_REL_RULES ON UE_REL_RULES.id_rule = UE_RELS.id_rule\n" +
                "    \tJOIN UE_CHANGE_LOG ON UE_RELS.id_change = UE_CHANGE_LOG.id_change\n" +
                "    \tJOIN UE_USERS ON UE_CHANGE_LOG.id_user = UE_USERS.id_user\n" +
                "WHERE %s = '%s'", column, value);

        Connection connection = DB.getConnection("invetory", true);
        List<FindByInvNumber> result = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery(sqlSelect);
        while (resultSet.next()) {
            result.add(new FindByInvNumber(
                    resultSet.getString("ue_inv_num"),
                    resultSet.getString("type_name"),
                    resultSet.getString("prop_name"),
                    resultSet.getString("prop_ue_value"),
                    resultSet.getString("prop_data_type"),
                    resultSet.getString("name_rule"),
                    resultSet.getString("user_name"),
                    resultSet.getString("user_surname"),
                    resultSet.getString("mod_date")
            ));
        }
        resultSet.close();
        connection.close();
        return result;
    }
}
