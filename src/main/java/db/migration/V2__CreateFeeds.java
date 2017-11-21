package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.Statement;

import static org.jooq.impl.DSL.*;

public class V2__CreateFeeds implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try(Statement stmt = connection.createStatement()) {

            DSLContext create = DSL.using(connection);
            String ddl = create.createTable(table("feeds"))
                    .column(field("feed_id", SQLDataType.BIGINT.identity(true)))
                    .column(field("name", SQLDataType.VARCHAR(255).nullable(false)))
                    .column(field("icon_url", SQLDataType.VARCHAR(255).nullable(true)))
                    .constraints(
                            constraint().primaryKey(field("feed_id"))
                    ).getSQL();
            stmt.execute(ddl);
        }
    }
}
