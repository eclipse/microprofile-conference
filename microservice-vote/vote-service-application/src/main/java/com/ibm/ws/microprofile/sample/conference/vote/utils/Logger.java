package com.ibm.ws.microprofile.sample.conference.vote.utils;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Log
@Interceptor
public class Logger {

	@AroundInvoke
	public Object Log(InvocationContext context) throws Exception {
	    Method method = context.getMethod();
	    String clazz = method.getDeclaringClass().getName();
	    String mthd =  clazz + ":" + method.getName();
     	String parms = Arrays.toString(context.getParameters());
		
		
	       try {
	           Object result = context.proceed();
	           System.out.println( "Call to " + mthd + "(" + parms + ") returned " + result);
	           return result;
	        }
	        catch (Exception e) {
               System.out.println("Call to " + mthd + "(" + parms + ") threw " + e.toString() );
	           throw e;
	        }
	}

}
