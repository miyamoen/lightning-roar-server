package net.unit8.lightningroar.dao;

import net.unit8.lightningroar.DomaConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;

@Dao(config = DomaConfig.class)
public interface SubscriptionDao {
    @Insert(sqlFile = true)
    int insert(String account, Long feedId);

    @Delete(sqlFile = true)
    int delete(String account, Long feedId);
}
