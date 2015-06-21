package controllers;

import actions.Authenticate;
import models.Job;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.jobs.add;
import views.html.jobs.index;

/**
 * Created by Daniel on 2015-06-15.
 */
@Authenticate
public class Jobs extends Controller {

    private static Form<Job> jobForm = Form.form(Job.class);

    public Result index() {
        return ok(index.render());
    }

    public Result add() {
        return ok(add.render(jobForm));
    }

    public Result create() {
        Form<Job> form = jobForm.bindFromRequest();
        if (form.hasErrors()) {
            if (request().accepts("application/json")) {
                return badRequest(form.errorsAsJson());
            } else {
                return badRequest(add.render(form));
            }
        } else {
            Job job = form.get();
            User user = (User) ctx().args.get("user");
            if (job.owner == null) {
                job.owner = user;
            }

            job.save();
            if (request().accepts("text/html")) {
                return redirect(routes.Jobs.get(job.id));
            } else {
                return ok(job.toJson());
            }
        }
    }

    public Result get(long id) {
        Job job = Job.find.byId(id);
        User requester = (User) ctx().args.get("user");
        if (job == null) {
            return notFound();
        } else {
            if (request().accepts("text/html")) {
                return TODO;
            } else {
                return ok(job.toJson(requester)).as("application/json");
            }
        }
    }

}
