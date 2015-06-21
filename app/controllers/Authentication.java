package controllers;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.authentication.login;

/**
 * Created by Daniel on 2015-06-20.
 */
public class Authentication extends Controller {

    private static Form<User> loginForm = Form.form(User.class, User.LoginStep.class);

    public Result login() {
        // TODO: check if we are already logged in
        return ok(login.render(loginForm));
    }

    public Result authenticate() {
        Form<User> form = loginForm.bindFromRequest();
        User filled = form.get();
        User user = User.find.where().eq("email", filled.email).findUnique();
        if(user != null) {
            boolean correct = BCrypt.checkpw(filled.password, user.encryptedPassword);
            if(correct) {
                session().clear();
                session("email", user.email);
                return TODO; // TODO: redirect
            } else {
                form.reject("Invalid email or password.");
                return badRequest(login.render(form));
            }
        } else {
            // TODO: check random thing to prevent timing attacks
            form.reject("Invalid email or password.");
            return badRequest(login.render(form));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result logout() {
        session().clear();
        return redirect(routes.Authentication.login()); // TODO: maybe redirect to home instead?
    }

}
