package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Daniel on 2015-06-15.
 */
@Entity
public class Contract extends Model {

    @Id
    public Long id;
    @ManyToOne
    @Constraints.Required
    public User user;
    @ManyToOne
    @Constraints.Required
    public Job job;
    public Boolean completed = false;
    public Integer workerRating;
    public String workerComment;
    public Integer ownerRating;
    public String ownerComment;

    public Contract(User user, Job job) {
        this.user = user;
        this.job = job;
    }

    public static Finder<Long, Contract> find = new Finder<>(Contract.class);
}
