package net.unit8.lightningroar.dao;

import net.unit8.lightningroar.DomaConfig;
import net.unit8.lightningroar.entity.FeedEntry;
import net.unit8.lightningroar.entity.UserFeedEntry;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

@Dao(config = DomaConfig.class)
public interface FeedEntryDao {
    @Select
    List<UserFeedEntry> selectByAccount(String account, SelectOptions options);

    @Insert
    int insert(FeedEntry feedEntry);

    @Delete
    int delete(FeedEntry feedEntry);
}
