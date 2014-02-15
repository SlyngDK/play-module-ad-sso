package dk.slyng.play.module.adsso.secured;

import play.api.templates.Html;

public interface SecuredInf {

    public boolean isLoggedIn();

    public void userLoggedIn(String username);

    public Html getHTML();
}
