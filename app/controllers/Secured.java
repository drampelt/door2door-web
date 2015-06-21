package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by Daniel on 2015-06-21.
 */
public class Secured extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        return context.session().get("email");
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return redirect(routes.Authentication.login());
    }
}
