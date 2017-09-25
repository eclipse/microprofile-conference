/*
 * Copyright (c) 2016 IBM, and others
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

import java.util.Optional;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class CredentialsProducer {
    
	@Resource(lookup="cloudant/url")
    protected String resourceUrl;

    @Resource(lookup="cloudant/username")
    protected String resourceUsername;

    @Resource(lookup="cloudant/password")
    protected String resourcePassword;
    
	@Inject
    @ConfigProperty(name="VCAP_SERVICES")  
    Optional <Credentials> cred;
	
    @Produces
    public Credentials newCredentials() {
        Credentials credentials = cred.orElse(null);
        if (credentials == null) {
        	if ( (("${env.CLOUDANT_URL}").equals(resourceUrl)) && (("${env.CLOUDANT_USERNAME}").equals(resourceUsername)) 
        			&& ( ("${env.CLOUDANT_PASSWORD}").equals(resourcePassword) ) )
        		credentials = null;
        	else
        		credentials = new Credentials(resourceUsername, resourcePassword, resourceUrl);
        } 
        return credentials;
    }
}
