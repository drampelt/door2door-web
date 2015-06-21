package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Job;
import models.User;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.jobs.add;
import views.html.jobs.index;

/**
 * Created by Daniel on 2015-06-15.
 */
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
        if(form.hasErrors()) {
            if (request().accepts("application/json")) {
                return badRequest(form.errorsAsJson());
            } else {
                return badRequest(add.render(form));
            }
        } else {
            Job job = form.get();
            User user = User.find.all().get(0);
            if(job.owner == null) { job.owner = user; }

            job.save();
            if(request().accepts("application/json")) {
                return ok(job.toJson());
            } else {
                return redirect(routes.Jobs.get(job.id));
            }
        }
    }

    public Result get(long id) {
        Job job = Job.find.byId(id);
        User requester = User.find.all().get(0);
        if(job == null) {
            return notFound();
        } else {
            if(request().accepts("text/html")) {
                return TODO;
            } else {
                return ok(job.toJson(requester)).as("application/json");
            }
        }
    }

}
