package io.microprofile.showcase.vote.persistence.couch;

import javax.json.JsonObject;

import org.eclipse.microprofile.config.spi.Converter;

public class CredentialsConverter implements Converter<Credentials> {

	@Override
	public Credentials convert(String vcapServices) throws IllegalArgumentException {
		Credentials creds;
		try {
			if ( (vcapServices.isEmpty() || vcapServices == null) )
				return null;
			
			JsonObject obj = VCAPServices.getCredentials(vcapServices, "cloudantNoSQLDB", "test-cloudantNoSQLDB-000");
					
	        String username = obj.getJsonString("username").getString();
	        String password = obj.getJsonString("password").getString();
	        String url = obj.getJsonString("url").getString();
	        creds = new Credentials(username, password, url);
		} catch (InvalidCredentialsException e) {
			throw new IllegalArgumentException(e);
		}
		return creds;
	}

}
