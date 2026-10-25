package com.patterns.builder.simulation2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RequestHandler {
    boolean handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
