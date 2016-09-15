package io.microprofile.showcase.bootstrap;

import javax.json.JsonObject;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public abstract class JsonWrapper {

    protected JsonObject underlying;

    private int id;

    public JsonWrapper(JsonObject underlying) {
        this.underlying = underlying;
    }

    JsonObject getUnderlying() {
        return underlying;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }
}
