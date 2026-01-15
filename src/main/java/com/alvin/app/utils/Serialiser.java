package com.alvin.app.utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Serialiser {
    public static <T> void writeClass(T obj, Path path){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String serialised = mapper.writeValueAsString(obj);
            Files.writeString(path ,serialised);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
