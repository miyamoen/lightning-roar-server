SELECT U.user_id, F.*
FROM feed_entries F
JOIN user_feed_entries UF ON UF.entry_id = F.entry_id
JOIN users U
WHERE U.account = /*account*/'account'
