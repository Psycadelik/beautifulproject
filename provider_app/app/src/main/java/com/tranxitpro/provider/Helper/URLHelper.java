package com.tranxitpro.provider.Helper;

/**
 * Created by jayakumar on 26/12/16.
 */

public class URLHelper {
    public static String base = "https://schedule.tranxit.co/";
//    public static String base = "https://dev.tranxit.co/";
    public static String login = base+"api/provider/oauth/token";
    public static String register = base+"api/provider/register";
    public static String USER_PROFILE_API = base+"api/provider/profile";
    public static String UPDATE_AVAILABILITY_API = base+"api/provider/profile/available";
    public static String GET_HISTORY_API = base+"api/provider/requests/history";
    public static String GET_HISTORY_DETAILS_API = base+"api/provider/requests/history/details";
    public static String CHANGE_PASSWORD_API = base+"api/provider/profile/password";
    public static final String UPCOMING_TRIP_DETAILS = base+"api/provider/requests/upcoming/details";
    public static final String UPCOMING_TRIPS = base+"api/provider/requests/upcoming";
    public static final String CANCEL_REQUEST_API = base+"api/provider/cancel";
    public static final String TARGET_API = base+"api/provider/target";
}
