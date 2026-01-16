package com.alvin.app.utils;
import com.alvin.app.Order;
import com.alvin.app.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeSerializer {
    public enum class_type{
        PRODUCT , ORDER
    }
    public static <T> T readClass(Path path , class_type type) throws IOException, ClassCastException{
        ObjectMapper mapper = new ObjectMapper();
        String obj = Files.readString(path);

        if(type == class_type.ORDER){
            Order class_read = mapper.readValue(obj , Order.class);
            return (T) class_read;
        }

        if(type == class_type.PRODUCT){
            Product class_read = mapper.readValue(obj , Product.class);
            return (T) class_read;
        }
        return null;
    }


}
