package io.microprofile.showcase.bootstrap;

import javax.json.JsonObject;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class Schedule extends JsonWrapper {


    private String sessionId;

    public Schedule(final JsonObject underlying) {
        super(underlying);
    }

    public String getDate() {
        return underlying.getString("date");
    }

    public String getStartTime() {
        return underlying.getString("startTime");
    }

    public String getVenue() {
        return underlying.getString("room");
    }

    public double getLength() {
        return underlying.getJsonNumber("length").doubleValue();
    }

    public String getSessionId() {
        return sessionId;
    }

    void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return getId() + "::" + getDate() + "::" + getStartTime();
    }

}
