package models;

import com.avaje.ebean.Model;
import flexjson.JSON;
import flexjson.JSONSerializer;
import play.data.validation.Constraints;
import util.BaseJSONSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2015-06-15.
 */
@Entity
public class Job extends Model implements JsonSerializable {

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @ManyToOne
//    @Constraints.Required
    public User owner;

    @Constraints.Required
    @Constraints.Min(1)
    public Integer positions;

    @Constraints.Required
    @Constraints.Min(1)
    public Integer payout;

    @Constraints.Required
    public String description;

    @Constraints.Required
    public String location;

    public String latitude;

    public String longitude;

    @OneToMany
    public List<Contract> contracts = new ArrayList<>();

    public Job(String name, int positions, int payout, User owner, String description, String location) {
        this.name = name;
        this.owner = owner;
        this.positions = positions;
        this.payout = payout;
        this.description = description;
        this.location = location;
    }

    public static Finder<Long, Job> find = new Finder<>(Job.class);

    @JSON
    public int getPositionsLeft() {
        return positions - contracts.size();
    }

    @Override
    public String toJson() {
        return new BaseJSONSerializer().serialize(this);
    }

    public String toJson(User requester) {
        JSONSerializer serializer = new BaseJSONSerializer();
        if(owner != null && requester != null && requester.id == owner.id) serializer.include("contracts");
        return serializer.serialize(this);
    }
}
