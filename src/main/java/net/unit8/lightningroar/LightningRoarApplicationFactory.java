package net.unit8.lightningroar;

import enkan.Application;
import enkan.application.WebApplication;
import enkan.config.ApplicationFactory;
import enkan.endpoint.ResourceEndpoint;
import enkan.middleware.*;
import enkan.middleware.doma2.DomaTransactionMiddleware;
import enkan.middleware.metrics.MetricsMiddleware;
import enkan.security.bouncr.BouncrBackend;
import enkan.system.inject.ComponentInjector;
import kotowari.middleware.*;
import kotowari.middleware.serdes.ToStringBodyWriter;
import kotowari.routing.Routes;
import net.unit8.lightningroar.controller.FeedEntryController;
import net.unit8.lightningroar.controller.FeedController;
import net.unit8.lightningroar.middleware.MappingExceptionMiddleware;

import java.util.Arrays;
import java.util.HashSet;

import static enkan.util.BeanBuilder.*;
import static enkan.util.Predicates.*;

public class LightningRoarApplicationFactory implements ApplicationFactory {
    @Override
    public Application create(ComponentInjector injector) {
        WebApplication app = new WebApplication();
        Routes routes = Routes.define("/roar" , r -> {
            r.get("/feed/all").to(FeedEntryController.class, "list");
            r.post("/feed/:feedId").to(FeedEntryController.class, "create");
            r.post("/feed/:feedId/subscribe").to(FeedController.class, "subscribe");
            r.post("/feed/:feedId/unsubscribe").to(FeedController.class, "unsubscribe");

            r.get("/feeds").to(FeedController.class, "list");
            r.post("/feeds").to(FeedController.class, "create");
        }).compile();

        // Enkan
        app.use(new DefaultCharsetMiddleware());
        app.use(new MetricsMiddleware<>());
        app.use(NONE, new ServiceUnavailableMiddleware<>(new ResourceEndpoint("/public/html/503.html")));
        app.use(envIn("development"), new LazyLoadMiddleware<>("enkan.middleware.devel.StacktraceMiddleware"));
        app.use(envIn("development"), new LazyLoadMiddleware<>("enkan.middleware.devel.TraceWebMiddleware"));
        app.use(new TraceMiddleware<>());
        app.use(new ContentTypeMiddleware());
        app.use(new ParamsMiddleware());
        app.use(new MultipartParamsMiddleware());
        app.use(new MethodOverrideMiddleware());
        app.use(new NormalizationMiddleware());
        app.use(new NestedParamsMiddleware());
        app.use(new CookiesMiddleware());

        app.use(builder(new ContentNegotiationMiddleware())
                .set(ContentNegotiationMiddleware::setAllowedLanguages,
                        new HashSet<>(Arrays.asList("en", "ja")))
                .build());

        BouncrBackend bouncrBackend = new BouncrBackend();
        injector.inject(bouncrBackend);
        app.use(new AuthenticationMiddleware<>(Arrays.asList(bouncrBackend)));
        // Kotowari
        app.use(new ResourceMiddleware());
        app.use(builder(new RenderTemplateMiddleware())
                .build());
        app.use(new RoutingMiddleware(routes));
        app.use(new DomaTransactionMiddleware<>());
        // app.use(new FormMiddleware());
        app.use(builder(new SerDesMiddleware())
                .set(SerDesMiddleware::setBodyWriters, new ToStringBodyWriter())
                .build());
        app.use(new MappingExceptionMiddleware<>());
        // app.use(new ValidateFormMiddleware());
        app.use(new ControllerInvokerMiddleware(injector));

        return app;
    }
}
