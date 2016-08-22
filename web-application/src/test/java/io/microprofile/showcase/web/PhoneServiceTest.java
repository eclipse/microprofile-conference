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
package io.microprofile.showcase.web;


import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

/**
 * Arquillian will start the container, deploy all @Deployment bundles, then run all the @Test methods.
 * <p>
 * A strong value-add for Arquillian is that the test is abstracted from the server.
 * It is possible to rerun the same test against multiple adapters or server configurations.
 * <p>
 * A second value-add is it is possible to build WebArchives that are slim and trim and therefore
 * isolate the functionality being tested.  This also makes it easier to swap out one implementation
 * of a class for another allowing for easy mocking.
 */
@RunWith(Arquillian.class)
public class PhoneServiceTest extends Assert {

    /**
     * ShrinkWrap is used to create a war file on the fly.
     * <p>
     * The API is quite expressive and can build any possible
     * flavor of war file.  It can quite easily return a rebuilt
     * war file as well.
     * <p>
     * More than one @Deployment method is allowed.
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addClasses(Phone.class, PhoneService.class);
    }

    /**
     * This URL will contain the following URL data
     * <p>
     * - http://<host>:<port>/<webapp>/
     * <p>
     * This allows the test itself to be agnostic of server information or even
     * the name of the webapp
     */
    @ArquillianResource
    private URL webappUrl;

    @Test
    public void getPhoneList() throws Exception {

        final WebClient webClient = WebClient.create(webappUrl.toURI());
        webClient.accept(MediaType.APPLICATION_JSON);

        final Collection<? extends Phone> phones = webClient.path("phone/list").getCollection(Phone.class);

        assertEquals(5, phones.size());

        final Iterator<? extends Phone> it = phones.iterator();
        assertEquals("motorola-xoom-with-wi-fi", it.next().getId());
        assertEquals("motorola-xoom", it.next().getId());
        assertEquals("samsung-galaxy-tab", it.next().getId());
        assertEquals("droid-pro-by-motorola", it.next().getId());
        assertEquals("t-mobile-g2", it.next().getId());

    }

}
