/*
 * Copyright 2016 Microprofile.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.microprofile.showcase.vote.persistence.couch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.stream.JsonGenerator;

public class CouchID {

    private final String id;
    private final String revision;

    public CouchID(String id, String revision) {
        this.id = id;
        this.revision = revision;
    }

    public String getId() {
        return id;
    }

    public String getRevision() {
        return revision;
    }

    public static CouchID fromJSON(InputStream is) {
        JsonReader rdr = null;
        try {
            rdr = Json.createReader(is);
            JsonObject couchIDJson = rdr.readObject();

            JsonString idJson = couchIDJson.getJsonString("id");
            JsonString revJson = couchIDJson.getJsonString("rev");
            CouchID attendee = new CouchID(idJson.getString(), revJson.getString());
            return attendee;
        } finally {
            if (rdr != null) {
                rdr.close();
            }
        }

    }

    public static CouchID fromJSON(String json) {
        ByteArrayInputStream bais;
        try {
            bais = new ByteArrayInputStream(json.getBytes("UTF-8"));
            return fromJSON(bais);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void toJSON(OutputStream os) {
        JsonGenerator jsonGenerator = Json.createGenerator(os);
        jsonGenerator.writeStartObject();
        if (id != null)
            jsonGenerator.write("id", id);
        if (revision != null)
            jsonGenerator.write("rev", revision);
        jsonGenerator.writeEnd();
        jsonGenerator.close();
    }

    public String toJSON() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        toJSON(baos);
        return baos.toString();
    }
}
