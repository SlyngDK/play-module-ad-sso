package dk.slyng.play.module.adsso.secured;


import play.twirl.api.Content;

public interface SecuredInf {

    boolean isLoggedIn();

    void userLoggedIn(String username);

    Content getHTML();
}
