package net.unit8.lightningroar.controller;

import enkan.data.HttpResponse;
import enkan.collection.Parameters;
import enkan.component.BeansConverter;
import enkan.component.doma2.DomaProvider;
import enkan.security.bouncr.UserPermissionPrincipal;
import net.unit8.lightningroar.boundary.FeedEntryCreateRequest;
import net.unit8.lightningroar.dao.FeedEntryDao;
import net.unit8.lightningroar.dao.UserFeedEntryDao;
import net.unit8.lightningroar.entity.FeedEntry;
import net.unit8.lightningroar.entity.UserFeedEntry;
import org.seasar.doma.jdbc.SelectOptions;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static enkan.util.BeanBuilder.builder;


public class FeedEntryController {
    @Inject
    private DomaProvider daoProvider;

    @Inject
    private BeansConverter beansConverter;

    public List<UserFeedEntry> list(UserPermissionPrincipal principal) {
        FeedEntryDao feedEntryDao = daoProvider.getDao(FeedEntryDao.class);
        SelectOptions options = SelectOptions.get().limit(10);
        return feedEntryDao.selectByAccount(principal.getName(), options);
    }

    @Transactional
    public HttpResponse create(FeedEntryCreateRequest createRequest, Parameters params) {
        FeedEntryDao feedEntryDao = daoProvider.getDao(FeedEntryDao.class);
        UserFeedEntryDao userFeedEntryDao = daoProvider.getDao(UserFeedEntryDao.class);

        FeedEntry feedEntry = beansConverter.createFrom(createRequest, FeedEntry.class);
        feedEntry.setFeedId(params.getLong("feedId"));
        feedEntryDao.insert(feedEntry);
        userFeedEntryDao.insertBatch(feedEntry);

        return builder(HttpResponse.of("Ok"))
                .set(HttpResponse::setStatus, 200)
                .build();
    }
}
