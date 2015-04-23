package controllers;

import dk.slyng.play.module.adsso.ldap.LDAP;
import dk.slyng.play.module.adsso.ldap.LDAPUser;
import dk.slyng.play.module.adsso.secured.SecuredInf;
import play.mvc.Controller;
import play.twirl.api.Content;

import javax.naming.NamingException;

/**
 * Created by sb on 2/14/14.
 */
public class Login implements SecuredInf {
    @Override
    public boolean isLoggedIn() {
        String loggedIn = Controller.session("loggedIn");
        if (loggedIn == null)
            return false;
        return loggedIn.equals("true");
    }

    @Override
    public void userLoggedIn(String s) {

        Controller.session("loggedIn", "true");
        LDAP ldap = LDAP.getNewInstance();
        try {
            LDAPUser userDetail = ldap.getUserDetail(s);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Content getHTML() {
        return views.html.login.render();
    }
}
