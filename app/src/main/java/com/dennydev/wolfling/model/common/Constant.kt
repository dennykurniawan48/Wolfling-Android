package com.dennydev.wolfling.model.common

object Constant {
    const val BASE_URL = "https://wolfling.vercel.app"
    const val BASE_API = "${BASE_URL}/api"
    const val TWEET_URL = "${BASE_API}/tweet"
    const val REPLY_URL = "${BASE_API}/reply"
    const val FOLLOW_URL = "${BASE_API}/follow?userId={id}"
    const val NOTIFICATION_URL = "${BASE_API}/notification"
    const val NOTIFICATION_DETAIL_URL = "${NOTIFICATION_URL}/detail"
    const val DETAIL_TWEET_URL = "${BASE_API}/tweet/{idTweet}"
    const val LATEST_HOME_TWEET = "${TWEET_URL}?page={page}&limit=10"
    const val LOGIN_CREDENTIALS_URL = "${BASE_API}/login"
    const val LOGIN_GOOGLE_URL = "${BASE_API}/login/google?token={token}"
    const val REGISTER_CREDENTIALS_URL = "${BASE_API}/register"
    const val SET_USERNAME_URL = "${BASE_API}/setusername?username={username}"
    const val PROFILE_URL = "${BASE_API}/profile?username={username}"
    const val LIKE_URL = "${BASE_API}/like?id={idTweet}"
    const val RETWEET_URL = "${BASE_API}/retweet?id={idTweet}"
    const val PROFILE_TWEET = "${BASE_API}/profile/tweet?username={username}&page={page}"
    const val GOOGLE_CLIENT_ID = "417414264395-mhkn651qejkds4tbl9pcgsr46mbo4gtl.apps.googleusercontent.com"
    const val PUSHER_KEY = "ae7a1ae76051fa65036a"
    const val PUSHER_CLUSTER = "sa1"
}