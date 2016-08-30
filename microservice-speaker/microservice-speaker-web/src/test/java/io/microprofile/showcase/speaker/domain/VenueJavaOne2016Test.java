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
package io.microprofile.showcase.speaker.domain;

import io.microprofile.showcase.speaker.model.Speaker;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class VenueJavaOne2016Test {

    @Test
    public void test() throws Exception {

        final Set<Speaker> speakers = new VenueJavaOne2016().getSpeakers();

        Assert.assertFalse("Faile to get speakers", speakers.isEmpty());

        for (final Speaker speaker : speakers) {
            Assert.assertNotNull(speaker.getNameLast());

            System.out.println(speaker.getNameFirst() + " " + speaker.getNameLast());
        }
    }

}