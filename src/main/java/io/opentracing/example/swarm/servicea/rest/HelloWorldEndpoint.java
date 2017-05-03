package io.opentracing.example.swarm.servicea.rest;


import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.hawkular.apm.client.opentracing.APMTracer;

import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.web.servlet.filter.HttpServletRequestExtractAdapter;
import io.opentracing.propagation.Format;
import io.opentracing.util.GlobalTracer;


@Path("/hello")
public class HelloWorldEndpoint {

	static {
        GlobalTracer.register(new APMTracer());
	}

	@Context HttpServletRequest request;

	@GET
	@Produces("text/plain")
	public Response doGet() {
		Tracer tracer = GlobalTracer.get();
		SpanContext spanContext = tracer.extract(Format.Builtin.HTTP_HEADERS, new HttpServletRequestExtractAdapter(request));
		tracer
				.buildSpan("myspan")
				.asChildOf(spanContext)
				.start()
				.setTag("component", "mycomponent")
				.setOperationName("theoperation")
				.close();

		return Response.ok("Hello from WildFly Swarm!").build();
	}
}