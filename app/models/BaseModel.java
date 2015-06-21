package models;

import com.avaje.ebean.Model;
import flexjson.JSONSerializer;

/**
 * Created by Daniel on 2015-06-20.
 */
public abstract class BaseModel extends Model {

    public JSONSerializer generateBaseSerializer() {
        return new JSONSerializer().prettyPrint(true).exclude("*.class");
    }

}
