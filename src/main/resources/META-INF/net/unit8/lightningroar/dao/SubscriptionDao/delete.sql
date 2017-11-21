DELETE FROM subscriptions
WHERE user_id = (SELECT user_id FROM users WHERE account = /*account*/'account')
  AND feed_id = /*feedId*/1
