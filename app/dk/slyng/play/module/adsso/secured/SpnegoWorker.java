package dk.slyng.play.module.adsso.secured;

import net.jodah.expiringmap.ExpiringMap;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Used to manage the SSOContext, and init required thing.
 */
public class SpnegoWorker {
    public static Oid spnegoOid;

    private final String principal;
    private LoginContext loginContext;
    private Map<String, SSOContext> ssoContexts = ExpiringMap.builder()
            .expiration(30, TimeUnit.SECONDS)
            .expirationPolicy(ExpiringMap.ExpirationPolicy.ACCESSED)
            .build();

    public SpnegoWorker() {
        try {
            spnegoOid = new Oid("1.3.6.1.5.5.2");
        } catch (GSSException e) {
            e.printStackTrace();
        }

        Set<Principal> principals = new HashSet<Principal>();
        SecuredLoginConfiguration loginConfiguration = new SecuredLoginConfiguration();
        principal = loginConfiguration.getPrincipal();
        if (principal == null) {
            throw new NullPointerException("Principal can't be null.");
        }
        principals.add(new KerberosPrincipal(principal, KerberosPrincipal.KRB_NT_SRV_INST));
        Subject subject = new Subject(false, principals, new HashSet<Object>(), new HashSet<Object>());

        try {
            loginContext = new LoginContext("", subject, null, loginConfiguration);
            loginContext.login();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public SSOContext createSsoContext(String id) {
        SSOContext ssoContext = new SSOContext(loginContext.getSubject());
        ssoContexts.put(id, ssoContext);
        return ssoContext;
    }

    public SSOContext getSsoContext(String id) {
        return ssoContexts.get(id);
    }

    public void finishSsoContext(String id) {
        try {
            ssoContexts.get(id).dispose();
        } catch (GSSException e) {
            e.printStackTrace();
        }
        ssoContexts.remove(id);
    }
}
