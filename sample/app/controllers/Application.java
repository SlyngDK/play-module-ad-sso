package controllers;

import dk.slyng.play.module.adsso.ldap.LDAP;
import dk.slyng.play.module.adsso.secured.Secured;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Map;

public class Application extends Controller {

    @Secured(INF = Login.class)
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result login() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        LDAP ldap = LDAP.getNewInstance();
        boolean authenticate = ldap.authenticate(form.get("username")[0], form.get("password")[0]);
        if (authenticate)
            Controller.session("loggedIn", "true");
        return redirect(routes.Application.index());
    }

}
