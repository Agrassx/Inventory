package controllers;


import play.mvc.Controller;
import play.mvc.Result;
import views.html.general;
import views.html.index;

public class Application extends Controller {

    public Result index() {
        return ok(index.render(""));
    }

    public Result add() {
        return ok(general.render("Hello"));
    }

    public Result home() {
        return ok(general.render("Hello "+session().get("UserName")));
    }

}