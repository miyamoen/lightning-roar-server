package net.unit8.lightningroar.middleware;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import enkan.Middleware;
import enkan.MiddlewareChain;
import enkan.collection.Headers;
import enkan.data.ContentNegotiable;
import enkan.data.HttpRequest;
import enkan.data.HttpResponse;
import enkan.exception.UnreachableException;
import enkan.util.HttpRequestUtils;
import net.unit8.lightningroar.MalformedRequestException;
import org.seasar.doma.jdbc.NoResultException;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;

import static enkan.util.BeanBuilder.*;

@enkan.annotation.Middleware(name = "mappingException")
public class MappingExceptionMiddleware<RES> implements Middleware<HttpRequest, RES>{
    private HttpResponse createErrorResponse() {
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator generator = new JsonFactory().createGenerator(sw);

            generator.flush();

            return builder(HttpResponse.of(sw.toString()))
                    .set(HttpResponse::setStatus, 400)
                    .build();
        } catch(IOException e) {
            throw new UnreachableException(e);
        }

    }

    @Override
    public RES handle(HttpRequest request, MiddlewareChain chain) {
        try {
            return (RES) chain.next(request);
        } catch (Exception e) {
            MediaType mediaType = ContentNegotiable.class.cast(request).getMediaType();

            if (mediaType.getSubtype().equals("json")) {
                if (e instanceof MalformedRequestException) {
                    return (RES) builder(HttpResponse.of(((MalformedRequestException) e).toJson()))
                            .set(HttpResponse::setStatus, 400)
                            .set(HttpResponse::setHeaders, Headers.of(
                                    "Content-Type", "application/json"))
                            .build();
                } else if (e instanceof NoResultException) {
                    return (RES) builder(HttpResponse.of(""))
                            .set(HttpResponse::setStatus, 404)
                            .build();
                } else {
                    return (RES) builder(HttpResponse.of(e.getMessage()))
                            .set(HttpResponse::setStatus, 500)
                            .set(HttpResponse::setHeaders, Headers.of(
                                    "Content-Type", "application/json"))
                            .build();
                }
            } else {
                throw e;
            }
        }
    }
}
