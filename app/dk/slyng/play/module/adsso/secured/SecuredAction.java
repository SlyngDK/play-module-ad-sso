package dk.slyng.play.module.adsso.secured;

import org.apache.xerces.impl.dv.util.Base64;
import org.ietf.jgss.GSSException;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Content;

import java.security.PrivilegedActionException;
import java.util.UUID;

public class SecuredAction extends Action<Secured> {
    private static SpnegoWorker spnegoWorker = new SpnegoWorker();

    public SecuredAction() {
    }

    private Return before(final Http.Context ctx) {
        SecuredInf securedInf;

        try {
            securedInf = configuration.INF().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return new Return(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new Return(false);
        }
        if (securedInf == null)
            return new Return(badRequest("An error has occurred!!!"));

        byte[] outToken = null;
        if (securedInf.isLoggedIn()) {
            return new Return(true);
        } else if (ctx.request().getHeader("Authorization") != null) {
            String authorization = ctx.request().getHeader("Authorization");
            authorization = authorization.substring(authorization.indexOf(" ") + 1);

            byte[] authorizationBytes = Base64.decode(authorization);

            System.out.println("Input token size: " + authorizationBytes.length);

            String id = ctx.session().get("auth_id");
            if (id == null) {
                id = UUID.randomUUID().toString();
                ctx.session().put("auth_id", id);
            }

            SSOContext context = spnegoWorker.getSsoContext(id);
            if (context == null) {
                System.out.println("Creating new context.");
                System.out.println("Url: " + ctx.request().uri());
                context = spnegoWorker.createSsoContext(id);
            } else {
                System.out.println("Reuse context: ");

            }
            try {
                outToken = context.acceptSecContext(authorizationBytes);
            } catch (PrivilegedActionException e) {
                e.printStackTrace();
            }
            if (context.isEstablished()) {
                try {
                    ctx.session().remove("auth_id");
                    securedInf.userLoggedIn(context.getSrcName().toString());
                    context.dispose();
                    spnegoWorker.finishSsoContext(id);
                } catch (GSSException e) {
                    e.printStackTrace();
                }

                return new Return(true);
            }
        }
        if (outToken == null) {
            ctx.response().setHeader(Http.HeaderNames.WWW_AUTHENTICATE, "Negotiate");
        } else {
            ctx.response().setHeader(Http.HeaderNames.WWW_AUTHENTICATE, "Negotiate " + Base64.encode(outToken));
        }
        Content html = securedInf.getHTML();
        if (html == null) {
            html = new Content() {
                @Override
                public String body() {
                    return "Unauthorized";
                }

                @Override
                public String contentType() {
                    return "text/html";
                }
            };
        }
        return new Return(unauthorized(html).as("text/html"));
    }

    private void after(Http.Context ctx) {

    }

    @Override
    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        try {
            Return before = before(ctx);
            if (!before.ok) {
                return F.Promise.pure(before.result);
            }
            F.Promise<Result> result = delegate.call(ctx);
            after(ctx);
            return result;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private class Return {
        public final boolean ok;
        public Result result;

        private Return(boolean ok) {
            this.ok = ok;
        }

        private Return(Result result) {
            this.ok = false;
            this.result = result;
        }
    }
}