package controllers;

import play.db.DB;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.general;
import views.html.index;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class logInUp extends Controller {

    public Result logIn() {
        Http.RequestBody body = request().body();
        Map<String, String[]> request = body.asFormUrlEncoded();
        String usrMail = request.get("log_email")[0];
        String usrPass = request.get("log_pass")[0];
        String error = "";
        String resultPass = "";
        String resultName = "";
        String resultId = "";
        String sqlQuery = "SELECT id_user, user_mail, user_pass, user_name FROM UE_USERS WHERE user_mail = '"+usrMail+"'";
        Integer rowCount = 0;
        try {
            Connection connection = DB.getConnection("invetory", true);
            ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery();
            while (resultSet.next()) {
                resultPass = resultSet.getString("user_pass");
                resultName = resultSet.getString("user_name");
                resultId = resultSet.getString("id_user");
                rowCount++;
            }
            resultSet.close();
            connection.close();
            if (rowCount == 1 && resultPass.equals(usrPass)) {
                session().clear();
                session("connected", usrMail);
                session().put("UserName", resultName);
                session().put("UserId", resultId);
                return ok(general.render("Hello "+resultName));
            } else {
                return ok(index.apply("Invalid user or password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            error += e.getMessage();
            return badRequest(error);
        }
    }

    public Result signUp() {
        String sqlInsert = "INSERT INTO UE_USERS (user_name, user_surname, user_pass, user_mail) VALUES (?, ?, ?, ?)";
        Http.RequestBody body = request().body();
        Map<String, String[]> request = body.asFormUrlEncoded();
        if (request.get("reg_name")[0].equals("")) {
            flash("error", "Empty name");
            return redirect("/");
        }
        if (request.get("reg_surname")[0].equals("")) {
            flash("error", "Empty surname");
            return redirect("/");
        }
        if (request.get("reg_pass")[0].equals("")) {
            flash("error", "Empty pass");
            return redirect("/");
        }
        if (request.get("reg_mail")[0].equals("")) {
            flash("error", "Empty email");
            return redirect("/");
        }
        Connection connection = DB.getConnection("invetory", true);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setString(1,request.get("reg_name")[0]);
            preparedStatement.setString(2,request.get("reg_surname")[0]);
            preparedStatement.setString(3,request.get("reg_pass")[0]);
            preparedStatement.setString(4,request.get("reg_mail")[0]);
            int result = preparedStatement.executeUpdate();
            connection.close();
            if (result > 0) {
                return ok(index.apply("Now you can log in"));
            } else {
                return ok(index.apply("User already exists"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return badRequest(e.getMessage());
        }
    }

    public Result logOut() {
        session().clear();
        return redirect("/");
    }

}
