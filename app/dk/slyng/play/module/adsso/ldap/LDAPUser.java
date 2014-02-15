package dk.slyng.play.module.adsso.ldap;


import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;
import java.util.HashMap;
import java.util.Map;

public class LDAPUser {
    private String displayName;
    private String givenName;
    private String sAMAccountType;
    private String primaryGroupID;
    private String objectClass;
    private String badPasswordTime;
    private String objectCategory;
    private String cn;
    private String userAccountControl;
    private String userPrincipalName;
    private String dSCorePropagationData;
    private String codePage;
    private String distinguishedName;
    private String whenChanged;
    private String whenCreated;
    private String pwdLastSet;
    private String logonCount;
    private String accountExpires;
    private String lastLogoff;
    private String lastLogonTimestamp;
    private String lastLogon;
    private String uSNChanged;
    private String uSNCreated;
    private String countryCode;
    private String sAMAccountName;
    private String instanceType;
    private String badPwdCount;
    private String name;
    private String[] groups;

    private Map<String, String> unknowns;

    public LDAPUser(SearchResult result) {
        NamingEnumeration<String> iDs = result.getAttributes().getIDs();
        try {
            while (iDs.hasMore()) {
                String id = iDs.next();
                Attribute attribute = result.getAttributes().get(id);
                if (id.equals("memberOf")) {
                    groups = new String[attribute.size()];
                    for (int i = 0; i < attribute.size(); i++) {
                        String s = attribute.get(i).toString();
                        s = s.substring(0, s.indexOf(",")).replaceFirst("OU=", "").replaceFirst("CN=", "");
                        groups[i] = s;
                    }
                    continue;
                }
                set(id, (String) attribute.get());
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private void set(String key, String value) {
        switch (key) {
            case "displayName":
                displayName = value;
                break;
            case "givenName":
                givenName = value;
                break;
            case "sAMAccountType":
                sAMAccountType = value;
                break;
            case "primaryGroupID":
                primaryGroupID = value;
                break;
            case "objectClass":
                objectClass = value;
                break;
            case "badPasswordTime":
                badPasswordTime = value;
                break;
            case "objectCategory":
                objectCategory = value;
                break;
            case "cn":
                cn = value;
                break;
            case "userAccountControl":
                userAccountControl = value;
                break;
            case "userPrincipalName":
                userPrincipalName = value;
                break;
            case "dSCorePropagationData":
                dSCorePropagationData = value;
                break;
            case "codePage":
                codePage = value;
                break;
            case "distinguishedName":
                distinguishedName = value;
                break;
            case "whenChanged":
                whenChanged = value;
                break;
            case "whenCreated":
                whenCreated = value;
                break;
            case "pwdLastSet":
                pwdLastSet = value;
                break;
            case "logonCount":
                logonCount = value;
                break;
            case "accountExpires":
                accountExpires = value;
                break;
            case "lastLogoff":
                lastLogoff = value;
                break;
            case "lastLogonTimestamp":
                lastLogonTimestamp = value;
                break;
            case "lastLogon":
                lastLogon = value;
                break;
            case "uSNChanged":
                uSNChanged = value;
                break;
            case "uSNCreated":
                uSNCreated = value;
                break;
            case "countryCode":
                countryCode = value;
                break;
            case "sAMAccountName":
                sAMAccountName = value;
                break;
            case "instanceType":
                instanceType = value;
                break;
            case "badPwdCount":
                badPwdCount = value;
                break;
            case "name":
                name = value;
                break;
            default:
                if (unknowns == null) unknowns = new HashMap<>();
                unknowns.put(key, value);
        }
    }

    public boolean isMemberOf(String groupname) {
        if (groups != null)
            for (String group : groups) {
                if (group.equals(groupname))
                    return true;
            }
        return false;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getsAMAccountType() {
        return sAMAccountType;
    }

    public String getPrimaryGroupID() {
        return primaryGroupID;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public String getBadPasswordTime() {
        return badPasswordTime;
    }

    public String getObjectCategory() {
        return objectCategory;
    }

    public String getCn() {
        return cn;
    }

    public String getUserAccountControl() {
        return userAccountControl;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public String getdSCorePropagationData() {
        return dSCorePropagationData;
    }

    public String getCodePage() {
        return codePage;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public String getWhenChanged() {
        return whenChanged;
    }

    public String getWhenCreated() {
        return whenCreated;
    }

    public String getPwdLastSet() {
        return pwdLastSet;
    }

    public String getLogonCount() {
        return logonCount;
    }

    public String getAccountExpires() {
        return accountExpires;
    }

    public String getLastLogoff() {
        return lastLogoff;
    }

    public String getLastLogonTimestamp() {
        return lastLogonTimestamp;
    }

    public String getLastLogon() {
        return lastLogon;
    }

    public String getuSNChanged() {
        return uSNChanged;
    }

    public String getuSNCreated() {
        return uSNCreated;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getsAMAccountName() {
        return sAMAccountName;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getBadPwdCount() {
        return badPwdCount;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getUnknowns() {
        return unknowns;
    }
}

