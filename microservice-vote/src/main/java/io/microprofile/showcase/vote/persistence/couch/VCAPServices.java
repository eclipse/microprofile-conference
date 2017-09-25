package io.microprofile.showcase.vote.persistence.couch;

import java.io.StringReader;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class VCAPServices {
  
  public static JsonObject getCredentials(String vcapServicesEnv, String serviceType, String serviceName) throws InvalidCredentialsException {
	  JsonObject credentials = null;	  
	  JsonArray vcapServiceArray = Json.createReader(new StringReader(vcapServicesEnv)).readArray();
	  Iterator<JsonValue> vsItr = vcapServiceArray.iterator();
      while (vsItr.hasNext()) {    	  
    	  JsonObject vcapServices = (JsonObject) vsItr.next();
          JsonArray serviceObjectArray = vcapServices.getJsonArray(serviceType);
          if (serviceObjectArray == null)
        	  continue;

          JsonObject serviceObject = null;
          if (serviceName == null || serviceName.isEmpty()) {
             serviceObject = serviceObjectArray.getJsonObject(0);
          } else {
             Iterator<JsonValue> itr = serviceObjectArray.iterator();
             while (itr.hasNext()) {
                JsonObject object = (JsonObject) itr.next();
                if (serviceName.equals(object.getJsonString("name").getString())) {
                    serviceObject = object;
                }
             }
          }
          checkNotNull(serviceObject);
          credentials = serviceObject.getJsonObject("credentials");
          checkNotNull(credentials);
          if (serviceObject != null)
        	  break;
      }
      return credentials;
  }

  private static void checkNotNull(Object object) throws InvalidCredentialsException {
    if (object == null) {
      throw new InvalidCredentialsException("Unable to parse VCAP_SERVICES");
    }
  }
}
