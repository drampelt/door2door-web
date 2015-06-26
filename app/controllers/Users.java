package controllers;

import actions.Authenticate;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by Daniel on 2015-06-22.
 */
public class Users extends Controller {

    @Authenticate
    public Result me() {
        User user = (User) ctx().args.get("user");
        if(true) return ok(user.toJson()).as("text/json");
        if (request().accepts("application/activex")) {
            return TODO;
        } else {
            return ok(user.toJson()).as("text/json");
        }
    }

}
