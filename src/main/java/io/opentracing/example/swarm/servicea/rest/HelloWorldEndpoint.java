package io.opentracing.example.swarm.servicea.rest;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.hawkular.apm.client.opentracing.APMTracer;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;


@Path("/hello")
public class HelloWorldEndpoint {
	@Context HttpServletRequest request;

	@GET
	@Produces("text/plain")
	public Response doGet() {
		Tracer tracer = GlobalTracer.get();
		// TODO: check whether extracting from the http request _should_ render the same result as getting the request attribute
		// SpanContext spanContext = tracer.extract(Format.Builtin.HTTP_HEADERS, new HttpServletRequestExtractAdapter(request));
		SpanContext requestContext = (SpanContext) request.getAttribute("io.opentracing.contrib.web.servlet.filter.TracingFilter.activeSpanContext");
		Span businessSpan = tracer
				.buildSpan("myspan")
				.asChildOf(requestContext)
				.start()
				.setTag("component", "mycomponent")
				.setOperationName("theoperation");

		// do some business operation

		businessSpan.close();

		return Response.ok("Hello from WildFly Swarm!").build();
	}
}