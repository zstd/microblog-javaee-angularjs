package com.example.zstd.microblog.utils;

import javax.servlet.http.HttpServletResponse;

public class ServletUtils {

    public static final int METHOD_NOT_ALLOWED = 405;

    public enum Method {
        POST,GET,PUT,DELETE,PATCH
    }

    public static final void answerNotAllowed(HttpServletResponse response,Method...allowedMethods) {
        response.setStatus(METHOD_NOT_ALLOWED);
        response.setHeader("Allow",allowedMethods.toString());
    }
}
