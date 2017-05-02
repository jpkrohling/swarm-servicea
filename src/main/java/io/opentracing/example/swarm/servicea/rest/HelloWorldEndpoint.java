package io.opentracing.example.swarm.servicea.rest;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.opentracing.NoopTracerFactory;
import io.opentracing.util.GlobalTracer;


@Path("/hello")
public class HelloWorldEndpoint {

	static {
        GlobalTracer.register(NoopTracerFactory.create());
	}

	@GET
	@Produces("text/plain")
	public Response doGet() {
		System.out.println(GlobalTracer.get());
		return Response.ok("Hello from WildFly Swarm!").build();
	}
}