package net.unit8.lightningroar;

import enkan.Env;
import enkan.collection.OptionMap;
import enkan.component.ApplicationComponent;
import enkan.component.builtin.HmacEncoder;
import enkan.component.doma2.DomaProvider;
import enkan.component.flyway.FlywayMigration;
import enkan.component.freemarker.FreemarkerTemplateEngine;
import enkan.component.hikaricp.HikariCPComponent;
import enkan.component.jackson.JacksonBeansConverter;
import enkan.component.jetty.JettyComponent;
import enkan.component.metrics.MetricsComponent;
import enkan.config.EnkanSystemFactory;
import enkan.system.EnkanSystem;
import net.unit8.bouncr.sign.JsonWebToken;

import static enkan.component.ComponentRelationship.component;
import static enkan.util.BeanBuilder.builder;

public class LightningRoarSystemFactory implements EnkanSystemFactory {
    @Override
    public EnkanSystem create() {
        return EnkanSystem.of(
                "hmac", new HmacEncoder(),
                "doma", new DomaProvider(),
                "jackson", new JacksonBeansConverter(),
                "flyway", new FlywayMigration(),
                "template", new FreemarkerTemplateEngine(),
                "metrics", new MetricsComponent(),
                "jwt", new JsonWebToken(),
                "datasource", new HikariCPComponent(OptionMap.of(
                        "uri", Env.getString("JDBC_URL", "jdbc:h2:mem:test"),
                        "username", Env.get("JDBC_USER"),
                        "password", Env.get("JDBC_PASSWORD"))),
                "app", new ApplicationComponent("net.unit8.lightningroar.LightningRoarApplicationFactory"),
                "http", builder(new JettyComponent())
                        .set(JettyComponent::setPort, 3004)
                        .build()
        ).relationships(
                component("http").using("app"),
                component("app").using(
                        "datasource", "template", "doma", "jackson", "metrics", "jwt"),
                component("doma").using("datasource", "flyway"),
                component("flyway").using("datasource")
        );
    }
}
