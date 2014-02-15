package dk.slyng.play.module.adsso.ldap;

import play.Play;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

public class LDAP {

    private final String FQDN;
    private final String server;
    private final String username;
    private final String password;

    private LDAP() {
        FQDN = Play.application().configuration().getString("secured.ldap.fqdn");
        server = Play.application().configuration().getString("secured.ldap.server");
        username = Play.application().configuration().getString("secured.ldap.principal");
        password = Play.application().configuration().getString("secured.ldap.password");
    }

    public static LDAP getNewInstance() {
        return new LDAP();
    }

    private DirContext getContext(String username, String password) throws NamingException {
        Hashtable<String, String> ldapEnv = new Hashtable<>(11);
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, "ldap://" + server + ":389/" + toDC(FQDN));
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, username);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, password);
        //ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
//        ldapEnv.put(Context.SECURITY_PROTOCOL, "simple");
        return new InitialDirContext(ldapEnv);
    }

    private DirContext getContext() throws NamingException {
        return getContext(username + "," + toDC(FQDN), password);
    }

    private NamingEnumeration<SearchResult> search(String searchFilter, String[] attrs) throws NamingException {
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(attrs);

        DirContext context = getContext();
        NamingEnumeration<SearchResult> renum = context.search("", searchFilter, controls);

        context.close();
        return renum;
    }

    private NamingEnumeration<SearchResult> search(String ldapFilter) throws NamingException {
        String returnedAtts[] = new String[]{"displayName",
                "givenName",
                "sAMAccountType",
                "primaryGroupID",
                "objectClass",
                "badPasswordTime",
                "objectCategory",
                "cn",
                "userAccountControl",
                "userPrincipalName",
                "dSCorePropagationData",
                "codePage",
                "distinguishedName",
                "whenChanged",
                "whenCreated",
                "pwdLastSet",
                "logonCount",
                "accountExpires",
                "lastLogoff",
                "lastLogonTimestamp",
                "lastLogon",
                "uSNChanged",
                "uSNCreated",
                "countryCode",
                "sAMAccountName",
                "instanceType",
                "badPwdCount",
                "name",
                "memberOf"
        };
        return search(ldapFilter, returnedAtts);
    }

    public LDAPUser getUserDetail(String username) throws NamingException {
        NamingEnumeration<SearchResult> renum = search("(&(objectClass=user)(|(userPrincipalName=" + username + ")(sAMAccountName=" + username + ")))");
        if (!renum.hasMoreElements()) {
            if (Play.isDev())
                System.out.println("User: " + username + " not found!");
            renum.close();
            return null;
        }
        SearchResult result = renum.next();

        LDAPUser user = new LDAPUser(result);

        renum.close();
        return user;
    }

    private String toDC(String domainName) {
        StringBuilder buf = new StringBuilder();
        for (String token : domainName.split("\\.")) {
            if (token.length() == 0) continue;   // defensive check
            if (buf.length() > 0) buf.append(",");
            buf.append("DC=").append(token);
        }
        return buf.toString();
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            return false;

        try {
            LDAPUser userDetail = getUserDetail(username);
            if (userDetail == null)
                return false;

            DirContext context = getContext(userDetail.getDistinguishedName(), password);

            boolean result = context != null;

            if (result)
                context.close();

            return result;
        } catch (NamingException e) {
            return false;
        }
    }
}

