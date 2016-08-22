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


import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

import static javax.ejb.LockType.READ;

@Path("/phone")
@Singleton
@Lock(READ)
public class PhoneService {

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Phone> getPhones() {

        return Arrays.asList(
                new Phone(0, "", "motorola-xoom-with-wi-fi", "Motorola XOOM(tm) with Wi-Fi", "The Next, Next Generation\r\n\r\nExperience the future with Motorola XOOM with Wi-Fi, the world's first tablet powered by Android 3.0 (Honeycomb)."),

                new Phone(1, "", "motorola-xoom", "MOTOROLA XOOM(tm)", "The Next, Next Generation\n\nExperience the future with MOTOROLA XOOM, the world's first tablet powered by Android 3.0 (Honeycomb)."),

                new Phone(8, "", "samsung-galaxy-tab", "Samsung Galaxy Tab(tm)", "Feel Free to Tab(tm). The Samsung Galaxy Tab(tm) brings you an ultra-mobile entertainment experience through its 7\" display, high-power processor and Adobe(R) Flash(R) Player compatibility."),

                new Phone(11, "Verizon", "droid-pro-by-motorola", "DROID(tm) Pro by Motorola", "The next generation of DOES."),

                new Phone(18, "", "t-mobile-g2", "T-Mobile G2", "The T-Mobile G2 with Google is the first smartphone built for 4G speeds on T-Mobile's new network. Get the information you need, faster than you ever thought possible.")

        );

    }


}
