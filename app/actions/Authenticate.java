package actions;

import controllers.routes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import models.JsonError;
import models.User;
import play.Play;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Daniel on 2015-06-21.
 */
@With(Authenticate.AuthenticateAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticate {
    boolean loggedIn() default true;

    class AuthenticateAction extends Action<Authenticate> {

        @Override
        public F.Promise<Result> call(Http.Context context) throws Throwable {
            String email = context.session().get("email");

            if (email == null) {
                String[] authorization = context.request().headers().get("Authorization");
                if (authorization != null && authorization.length > 0) {
                    String[] split = authorization[0].split(" "); // Assuming only one authorization header
                    if (split.length == 2 && split[0].equals("Bearer")) {
                        String token = split[1];
                        try {
                            Jws<Claims> jws = Jwts.parser().setSigningKey(Play.application().configuration().getString("play.crypto.secret").getBytes()).parseClaimsJws(token);
                            Object tokenEmail = jws.getBody().get("email");
                            if (tokenEmail != null) email = tokenEmail.toString();
                        } catch (Exception ignored) {}
                    }
                }
            }

            if (email == null) { // Not logged in
                if (configuration.loggedIn()) {
                    if (context.request().accepts("text/html")) {
                        return F.Promise.pure(redirect(routes.Authentication.login()));
                    } else {
                        return F.Promise.pure(unauthorized(JsonError.UNAUTHORIZED).as("application/json"));
                    }
                } else {
                    return delegate.call(context);
                }
            } else {
                if (configuration.loggedIn()) {
                    User user = User.find.where().eq("email", email).findUnique();
                    if (user == null) { // For some reason that user doesn't exist any more
                        context.session().clear();
                        if (context.request().accepts("text/html")) {
                            return F.Promise.pure(redirect(routes.Authentication.login()));
                        } else {
                            return F.Promise.pure(unauthorized(JsonError.UNAUTHORIZED).as("application/json"));
                        }
                    } else {
                        context.args.put("user", user);
                        return delegate.call(context);
                    }
                } else {
                    if (context.request().accepts("text/html")) {
                        return F.Promise.pure(redirect("/")); // TODO: use home route
                    } else {
                        return F.Promise.pure(unauthorized(JsonError.UNAUTHORIZED).as("application/json"));
                    }
                }
            }
        }
    }
}
