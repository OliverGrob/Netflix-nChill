package com.codecool.netflixandchill.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {

    public JsonObject getJsonObject(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return (JsonObject) new JsonParser().parse(sb.toString());
    }

}
