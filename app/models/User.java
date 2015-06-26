package models;

import com.avaje.ebean.Model;
import flexjson.JSON;
import flexjson.JSONSerializer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import play.Play;
import play.data.validation.Constraints;
import util.BaseJSONSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 2015-06-15.
 */
@Entity
public class User extends Model implements JsonSerializable {
    public interface LoginStep {}
    public interface SignupStep {}

    @Id
    public Long id;

    @Constraints.Required(groups = SignupStep.class)
    public String firstName;

    @Constraints.Required(groups = SignupStep.class)
    public String lastName;

    @Column(unique = true)
    @Constraints.Email
    @Constraints.Required(groups = {SignupStep.class, LoginStep.class})
    @JSON(include = false)
    public String email;

    @JSON(include = false)
    public String encryptedPassword;

    @Transient
    @Constraints.Required(groups = {SignupStep.class, LoginStep.class})
    @Constraints.MinLength(8)
    public transient String password;
    @Transient
    @Constraints.Required(groups = SignupStep.class)
    @Constraints.MinLength(8)
    public transient String passwordConfirmation;

    @OneToMany(cascade = CascadeType.REMOVE)
    public List<Contract> contracts = new ArrayList<>();

    public User(String firstName, String lastName, String email, String password, String passwordConfirmation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public static Finder<Long, User> find = new Finder<>(User.class);

    @JSON(include = false)
    public String getToken() {
        return Jwts.builder()
                .claim("email", email)
                .setIssuedAt(new Date())
                .setAudience("https://door2door")
                .setIssuer("lt.danielrampe.door2door")
                .setSubject(id.toString())
                .signWith(SignatureAlgorithm.HS512, Play.application().configuration().getString("play.crypto.secret").getBytes())
                .compact();
    }

    @Override
    public String toJson() {
        return new BaseJSONSerializer().serialize(this);
    }

    public String toJson(boolean includeContracts, boolean includeToken) {
        JSONSerializer serializer = new BaseJSONSerializer();
        if (includeContracts) serializer.include("contracts");
        if (includeToken) serializer.include("token");
        return serializer.serialize(this);
    }
}
