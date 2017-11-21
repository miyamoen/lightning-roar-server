package net.unit8.lightningroar.dao;

import net.unit8.lightningroar.DomaConfig;
import net.unit8.lightningroar.entity.User;
import org.seasar.doma.*;

@Dao(config = DomaConfig.class)
public interface UserDao {
    @Select
    User selectByAccount(String account);

    @Insert
    int insert(User user);

    @Update
    int update(User user);

    @Delete
    int delete(User user);
}
