package models;

import flexjson.JSON;
import util.BaseJSONSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Daniel on 2015-06-21.
 */
public class JsonError {

    public static String UNAUTHORIZED = new JsonError("You must be logged in to do that.").toJson();
    public static String FORBIDDEN = new JsonError("You are not allowed to do that.").toJson();
    public static String NOT_FOUND = new JsonError("The resource you requested could not be found.").toJson();

    @JSON
    public List<String> errors;

    public JsonError() {
        errors = new ArrayList<>();
    }

    public JsonError(String... errors) {
        this.errors = Arrays.asList(errors);
    }

    public void add(String error) {
        errors.add(error);
    }

    public void clear() {
        errors.clear();
    }

    public String toJson() {
        return new BaseJSONSerializer().serialize(this);
    }
}
