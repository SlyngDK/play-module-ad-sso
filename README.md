# Play Module AD SSO

This is a play 2.3 module for kerberos SSO, with support for fallback.

It also contain a simple ldap class to get user details and authenticate, using username and password.


Here is a simple example of how to secure a 
```java
public class Application extends Controller {

   @Secured(INF = Login.class) //Annotation telling to protect this site. INF is the implementation of the SecuredInf, used to integrate to your pproject.
   public static Result index() {
      return ok(index.render("Your new application is ready."));
   }
}
```

Take and look at the sample and see how it works.

# Pre-Setup

1. Create a service account to your Play app in Active Directory. 
   On the Account page, select the User cannot change password and Password never expires check boxes. By preventing the password from expiring, you avoid having to recreate the keytab file (which you do in the next step) after the password is changed. Click OK to save the new user information.

2. Map the service principal name to the user account that you created, and then generate a keytab file by running the ktpass command on the domain controller.
```cmd
ktpass -princ <SPN(HTTP/www.domain.local@DOMAIN.LOCAL)> -out <path_to_keytab> -mapuser <account_name> -mapOp set -pass <account_password> -ptype KRB5_NT_PRINCIPAL -crypto All -kvno 0
```

Create a gpo to add the site to the intranet zone, to allow single sign on from the Windows PC.

1. Open \<Computer Configuration/Policies/Administrative Templates/Windows Components/Internet Explorer/Internet Control Panel/Security Page>
2. Set "Site to Zone Assignment List" to "Enabled"
3. Add the address to your Play app with a value of 1.
4. 

# Adding to your project

Add resolver and dependency.

```
libraryDependencies ++= Seq(
  "dk.slyng.play.module" % "play-module-ad-sso_2.11" % "0.2.0"
)

resolvers += Resolver.url("Play Module AD SSO Repository", url("http://SlyngDK.github.com/releases/"))(Resolver.ivyStylePatterns)
```
