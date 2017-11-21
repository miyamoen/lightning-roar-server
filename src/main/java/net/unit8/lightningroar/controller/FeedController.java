package net.unit8.lightningroar.controller;

import enkan.collection.Parameters;
import enkan.component.BeansConverter;
import enkan.component.doma2.DomaProvider;
import enkan.security.bouncr.UserPermissionPrincipal;
import net.unit8.lightningroar.boundary.FeedCreateRequest;
import net.unit8.lightningroar.dao.FeedDao;
import net.unit8.lightningroar.dao.SubscriptionDao;
import net.unit8.lightningroar.dao.UserDao;
import net.unit8.lightningroar.entity.Feed;
import net.unit8.lightningroar.entity.User;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static enkan.util.BeanBuilder.builder;

public class FeedController {
    @Inject
    private DomaProvider daoProvider;

    @Inject
    private BeansConverter beansConverter;

    public List<Feed> list() {
        FeedDao feedDao = daoProvider.getDao(FeedDao.class);
        return feedDao.selectAll();
    }

    @Transactional
    public Feed create(FeedCreateRequest createRequest) {
        FeedDao feedDao = daoProvider.getDao(FeedDao.class);
        Feed feed = beansConverter.createFrom(createRequest, Feed.class);
        feedDao.insert(feed);
        return feed;
    }

    @Transactional
    public void subscribe(UserPermissionPrincipal principal, Parameters params) {
        UserDao userDao = daoProvider.getDao(UserDao.class);
        User user = userDao.selectByAccount(principal.getName());
        if (user == null) {
            userDao.insert(builder(new User())
                    .set(User::setAccount, principal.getName())
                    .build());
        }

        SubscriptionDao subscriptionDao = daoProvider.getDao(SubscriptionDao.class);
        subscriptionDao.insert(principal.getName(), params.getLong("feedId"));
    }

    @Transactional
    public void unsubscribe(UserPermissionPrincipal principal, Parameters params) {
        SubscriptionDao subscriptionDao = daoProvider.getDao(SubscriptionDao.class);
        subscriptionDao.delete(principal.getName(), params.getLong("feedId"));
    }

}
