package com.alvin.app.utils;
import com.alvin.app.Order;
import com.alvin.app.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeSerialiser {
    enum class_type{
        PRODUCT , ORDER
    }
    public static <T> T readClass(Path path , class_type type){
        try {
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
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
