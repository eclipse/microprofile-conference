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
package io.microprofile.showcase.vote.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Log
@Interceptor
@Priority(Logger.PRIORITY)
public class Logger {

    static final int PRIORITY = 2001;

    @AroundInvoke
    public Object Log(InvocationContext context) throws Exception {
        Method method = context.getMethod();
        String clazz = method.getDeclaringClass().getName();
        String mthd = clazz + ":" + method.getName();
        String parms = Arrays.toString(context.getParameters());

        try {
            Object result = context.proceed();
            System.out.println("Call to " + mthd + "(" + parms + ") returned " + result);
            return result;
        } catch (Exception e) {
            System.out.println("Call to " + mthd + "(" + parms + ") threw " + e.toString());
            throw e;
        }
    }

}
