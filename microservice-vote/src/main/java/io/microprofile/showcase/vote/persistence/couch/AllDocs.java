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
import java.util.ArrayList;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class AllDocs {

    private final Collection<String> ids;

    public AllDocs(Collection<String> ids) {
        this.ids = ids;
    }

    public Collection<String> getIds() {
        return ids;
    }

    public static AllDocs fromJSON(InputStream is) {
        JsonReader rdr = null;
        try {
            rdr = Json.createReader(is);
            JsonObject couchIDJson = rdr.readObject();

            JsonArray rowsJson = couchIDJson.getJsonArray("rows");

            Collection<String> ids = new ArrayList<String>();
            for (int i = 0; i < rowsJson.size(); i++) {
                JsonObject value = rowsJson.getJsonObject(i);
                String id = value.getString("id");
                ids.add(id);
            }

            AllDocs allDocs = new AllDocs(ids);
            return allDocs;
        } finally {
            if (rdr != null) {
                rdr.close();
            }
        }

    }

    public static AllDocs fromJSON(String json) {
        ByteArrayInputStream bais;
        try {
            bais = new ByteArrayInputStream(json.getBytes("UTF-8"));
            return fromJSON(bais);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void toJSON(OutputStream os) {
        throw new UnsupportedOperationException();
    }

    public String toJSON() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        toJSON(baos);
        return baos.toString();
    }
}
