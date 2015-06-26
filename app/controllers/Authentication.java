package controllers;

import actions.Authenticate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication.login;
import views.html.authentication.signup;

import java.util.Date;

/**
 * Created by Daniel on 2015-06-20.
 */
public class Authentication extends Controller {

    private static Form<User> loginForm = Form.form(User.class, User.LoginStep.class);
    private static Form<User> signupForm = Form.form(User.class, User.SignupStep.class);

    @Authenticate(loggedIn = false)
    public Result login() {
        return ok(login.render(loginForm));
    }

    @Authenticate(loggedIn = false)
    public Result authenticate() {
        Form<User> form = loginForm.bindFromRequest();
        User filled = form.get();
        User user = User.find.where().eq("email", filled.email).findUnique();
        if (user != null) {
            boolean correct = BCrypt.checkpw(filled.password, user.encryptedPassword);
            if (correct) {
                if (request().accepts("text/html")) {
                    session().clear();
                    session("email", user.email);
                    return TODO; // TODO: redirect
                } else {
                    Logger.debug("test" + Play.application().configuration().getString("play.crypto.secret"));
                    return ok(user.toJson(false, true)).as("application/json");
                }
            } else {
                form.reject("Invalid email or password.");
                if (request().accepts("text/html")) {
                    return badRequest(login.render(form));
                } else {
                    return badRequest(form.errorsAsJson());
                }
            }
        } else {
            // TODO: check random thing to prevent timing attacks
            form.reject("Invalid email or password.");
            if (request().accepts("text/html")) {
                return badRequest(login.render(form));
            } else {
                return badRequest(form.errorsAsJson());
            }
        }
    }

    @Authenticate
    public Result logout() {
        session().clear();
        return redirect(routes.Authentication.login()); // TODO: maybe redirect to home instead?
    }

    @Authenticate(loggedIn = false)
    public Result signup() {
        return ok(signup.render(signupForm));
    }

    @Authenticate(loggedIn = false)
    public Result register() {
        Form<User> form = signupForm.bindFromRequest();
        if (form.hasErrors()) {
            if (request().accepts("text/html")) {
                return badRequest(signup.render(form));
            } else {
                return badRequest(form.errorsAsJson());
            }
        } else {
            User user = form.get();
            // TODO: password stuff
            User existing = User.find.where().eq("email", user.email).findUnique();
            if (existing != null) {
                form.reject("Email already taken.");
                if (request().accepts("text/html")) {
                    return badRequest(signup.render(form));
                } else {
                    return badRequest(form.errorsAsJson());
                }
            }

            if (!user.password.equals(user.passwordConfirmation)) {
                form.reject("Passwords must match.");
                if (request().accepts("text/html")) {
                    return badRequest(signup.render(form));
                } else {
                    return badRequest(form.errorsAsJson());
                }
            }

            user.encryptedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt());
            user.password = null;
            user.passwordConfirmation = null;
            user.save();

            if (request().accepts("text/html")) {
                session().put("email", user.email);
                return redirect("/"); // TODO: home route
            } else {
                return ok(user.toJson(false, true)).as("application/json");
            }
        }
    }

}
