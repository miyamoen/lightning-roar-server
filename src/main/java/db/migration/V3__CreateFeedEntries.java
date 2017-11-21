package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.Statement;

import static org.jooq.impl.DSL.*;

public class V3__CreateFeedEntries implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            DSLContext create = DSL.using(connection);
            String ddl = create.createTable(table("feed_entries"))
                    .column(field("entry_id", SQLDataType.BIGINT.identity(true)))
                    .column(field("feed_id", SQLDataType.BIGINT.nullable(false)))
                    .column(field("title", SQLDataType.VARCHAR(100).nullable(false)))
                    .column(field("summary", SQLDataType.VARCHAR(1000).nullable(false)))
                    .column(field("link", SQLDataType.VARCHAR(255).nullable(false)))
                    .column(field("updated", SQLDataType.TIMESTAMP.nullable(false)))
                    .constraints(
                            constraint().primaryKey(field("entry_id")),
                            constraint().foreignKey(field("feed_id")).references(table("feeds"), field("feed_id"))
                    ).getSQL();
            stmt.execute(ddl);

            ddl = create.createTable(table("user_feed_entries"))
                    .column(field("user_id", SQLDataType.BIGINT.nullable(false)))
                    .column(field("entry_id", SQLDataType.BIGINT.nullable(false)))
                    .constraints(
                            constraint().primaryKey(field("user_id"), field("entry_id")),
                            constraint().foreignKey(field("entry_id")).references(table("feed_entries"), field("entry_id")),
                            constraint().foreignKey(field("user_id")).references(table("users"), field("user_id"))
                    ).getSQL();
            stmt.execute(ddl);
        }

    }
}
