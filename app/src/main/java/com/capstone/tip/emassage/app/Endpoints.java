package com.capstone.tip.emassage.app;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class Endpoints {

    public static final String _ID = "{id}/";
    public static final String BASE_URL = "http://vlitztrom.pythonanywhere.com";
    //public static final String BASE_URL = "http://127.0.0.1:8000";

    public static final String API_URL = BASE_URL + "/emassage/api/";

    public static final String LOGIN = "user/login/";
    public static final String REGISTER = "user/register/";

    public static final String COURSES = "courses/";

    public static final String FORUMS = "forums/";
    public static final String FORUM = FORUMS + _ID;

    public static final String COMMENTS = "comments/";
    public static final String COMMENT = COMMENTS + _ID;

    public static final String UPVOTE_FORUM = FORUMS + "upvote/";
    public static final String DOWNVOTE_FORUM = FORUMS + "downvote/";

    public static final String GRADES = "grades/";
    public static final String GRADE = GRADES + _ID;

    public static final String FORUM_VOTES = "forum-vote/";
    public static final String FORUM_VOTE = FORUM_VOTES + _ID;

    public static final String COMMENT_VOTES = "comment-vote";
    public static final String COMMENT_VOTE = COMMENT_VOTES + _ID;

    public static final String VIDEOS = "video-simulations/";

    public static final String YOUTUBE_THUMBNAIL = "http://img.youtube.com/vi/{code}/hqdefault.jpg";

}
