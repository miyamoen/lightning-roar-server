INSERT INTO subscriptions(user_id, feed_id)
SELECT U.user_id, /*^feedId*/1
FROM users U
WHERE U.account = /*account*/'account'
