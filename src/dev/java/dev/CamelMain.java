package dev;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CamelMain {
    private static RouteBuilder routeBuilder = new RouteBuilder() {
        @Override
        public void configure() throws Exception {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            from("rss:http://b.hatena.ne.jp/hotentry.rss?consumer.delay=3600000&splitEntries=true")
                    .process(exchange -> {
                        SyndFeed syndFeed = (SyndFeed) exchange.getIn().getHeader("CamelRssFeed");
                        exchange.getIn().setBody(syndFeed.getEntries().stream()
                                .map(e -> {
                                    Map<String, Object> entry = new HashMap<>();
                                    entry.put("title", ((SyndEntry) e).getTitle());
                                    entry.put("summary", ((SyndEntry) e).getDescription().getValue());
                                    entry.put("link", ((SyndEntry) e).getLink());
                                    entry.put("updated", sdf.format(((SyndEntry) e).getPublishedDate()));
                                    return entry;
                                })
                                .collect(Collectors.toList()));
                    })
                    .removeHeader("CamelRssFeed")
                    .marshal()
                    .json(JsonLibrary.Jackson)
                    .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                    .setHeader("Accept", constant("application/json"))
                    .to("log:Hoge?showAll=true")
                    .to("http4:localhost:3004/feed/1");

        }
    };

    public static void main(String[] args) throws Exception {
        Main camel = new Main();
        camel.addRouteBuilder(routeBuilder);
        camel.run();
        camel.shutdown();
    }
}
