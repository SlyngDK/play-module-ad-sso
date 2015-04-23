package dk.slyng.play.module.adsso.secured;

import play.Play;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;

class SecuredLoginConfiguration extends Configuration {

    private final String principal;

    public SecuredLoginConfiguration() {
        principal = Play.application().configuration().getString("secured.krb5.principal");
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {

        Map<String, String> options = new HashMap<>();
        options.put("realm", Play.application().configuration().getString("secured.krb5.realm"));
        options.put("keyTab", Play.application().configuration().getString("secured.krb5.keyTab"));
        options.put("principal", principal);
        options.put("password", Play.application().configuration().getString("secured.krb5.password"));
        options.put("useKeyTab", "true");
        options.put("storeKey", "true");
        options.put("doNotPrompt", "true");
        options.put("useTicketCache", "true");
        options.put("renewTGT", "true");
        options.put("isInitiator", "false");
        options.put("debug", Play.application().configuration().getString("secured.krb5.debug", "false"));

        return new AppConfigurationEntry[]{
                new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
                        AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                        options),};
    }

    public String getPrincipal() {
        return principal;
    }
}