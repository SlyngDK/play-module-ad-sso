package dk.slyng.play.module.adsso.secured;

import org.ietf.jgss.*;

import javax.security.auth.Subject;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * Wrapper for the GSS Kerberos context.
 */
public class SSOContext implements PrivilegedExceptionAction<byte[]> {
    private final Subject subject;
    private final GSSManager manager = GSSManager.getInstance();
    private GSSCredential serverCreds;
    private GSSContext context;
    private byte[] token;


    public SSOContext(Subject subject) {
        this.subject = subject;
    }

    public synchronized byte[] acceptSecContext(byte[] token) throws PrivilegedActionException {
        this.token = token;
        return Subject.doAs(subject, this);
    }

    @Override
    public byte[] run() throws Exception {
        if (context == null) {
            try {
                serverCreds = manager.createCredential(null,
                        GSSCredential.DEFAULT_LIFETIME,
                        SpnegoWorker.spnegoOid,
                        GSSCredential.ACCEPT_ONLY);
                context = manager.createContext(
                        serverCreds);
            } catch (GSSException e) {
                e.printStackTrace();
            }
        }
        return context.acceptSecContext(token, 0, token.length);
    }

    public boolean isEstablished() {
        return context != null && context.isEstablished();
    }

    public GSSName getSrcName() throws GSSException {
        if (context != null) {
            return context.getSrcName();
        }
        return null;
    }

    public GSSName getTargName() throws GSSException {
        if (context != null) {
            return context.getTargName();
        }
        return null;
    }

    public boolean getMutualAuthState() {
        return context != null && context.getMutualAuthState();
    }

    public byte[] unwrap(byte[] token, int i, int length, MessageProp prop) throws GSSException {
        if (context != null) {
            return context.unwrap(token, i, length, prop);
        }
        return null;
    }

    public byte[] wrap(byte[] reply, int i, int length, MessageProp prop) throws GSSException {
        if (context != null) {
            return context.wrap(reply, i, length, prop);
        }
        return null;
    }

    public void dispose() throws GSSException {
        context.dispose();
    }
}
