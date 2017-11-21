package net.unit8.lightningroar.dao;

import net.unit8.lightningroar.DomaConfig;
import net.unit8.lightningroar.entity.FeedEntry;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;

@Dao(config = DomaConfig.class)
public interface UserFeedEntryDao {
    @Insert(sqlFile = true)
    int insertBatch(FeedEntry feedEntry);
}
