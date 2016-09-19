/*
 * (C) Copyright IBM Corporation 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.ws.microprofile.sample.conference.vote.persistence.couch;

import static com.ibm.ws.microprofile.sample.conference.vote.utils.Debug.isDebugEnabled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AllDocsProvider implements MessageBodyReader<AllDocs>, MessageBodyWriter<AllDocs> {

	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("AP.isReadable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(AllDocs.class));
		return clazz.equals(AllDocs.class);
	}

	@Override
	public AllDocs readFrom(Class<AllDocs> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream is) throws IOException, WebApplicationException {
		return AllDocs.fromJSON(is);
	}
	
	@Override
	public long getSize(AllDocs attendee, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("AP.getSize() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType);
		return 0;
	}

	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("AP.isWriteable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(AllDocs.class));
		return clazz.equals(AllDocs.class);
	}

	@Override
	public void writeTo(AllDocs CouchAllDocs, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> map, OutputStream os) throws IOException, WebApplicationException {
		CouchAllDocs.toJSON(os);
	}
}
