package util;

import flexjson.JSONSerializer;

/**
 * Created by Daniel on 2015-06-20.
 */
public class BaseJSONSerializer extends JSONSerializer {

    public BaseJSONSerializer() {
        super();
        prettyPrint(true);
        exclude("*.class");
    }
}
