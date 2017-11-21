INSERT INTO user_feed_entries(user_id, entry_id)
SELECT S.user_id, /*^feedEntry.id*/1
FROM subscriptions S
WHERE S.feed_id = /*feedEntry.feedId*/1
