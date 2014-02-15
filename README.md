# H1 Play Module AD SSO

This is a play 2 module for kerberos SSO, with support for fallback.

It also contain a simple ldap class to get user details and authenticate.

```java
@Secured(INF = Login.class)
public static Result index() {
   return ok(index.render("Your new application is ready."));
}
```

Take and look at the sample and see how it works.