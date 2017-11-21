package net.unit8.lightningroar.dao;

import net.unit8.lightningroar.DomaConfig;
import net.unit8.lightningroar.entity.Feed;
import org.seasar.doma.*;

import java.util.List;

@Dao(config = DomaConfig.class)
public interface FeedDao {
    @Select
    List<Feed> selectAll();

    @Insert
    int insert(Feed feed);

    @Update
    int update(Feed feed);

    @Delete
    int delete(Feed feed);
}
