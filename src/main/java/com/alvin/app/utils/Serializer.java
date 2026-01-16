package com.alvin.app.utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Serializer {
    public static <T> void writeClass(T obj, Path path) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        String serialised = mapper.writeValueAsString(obj);
        Files.writeString(path ,serialised);
    }

}
