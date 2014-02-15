package dk.slyng.play.module.adsso.secured;

import play.mvc.With;

import java.lang.annotation.*;

@With(SecuredAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface Secured {
    Class<? extends SecuredInf> INF();
}
