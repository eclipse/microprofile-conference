package io.microprofile.showcase.bootstrap;

import javax.json.JsonObject;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public abstract class JsonWrapper {

    protected JsonObject underlying;

    private String id;

    public JsonWrapper(final JsonObject underlying) {
        this.underlying = underlying;
    }

    JsonObject getUnderlying() {
        return underlying;
    }

    public String getId() {
        return id;
    }

    void setId(final String id) {
        this.id = id;
    }
}
