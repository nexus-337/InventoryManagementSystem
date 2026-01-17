package com.alvin.app.utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IdGenerator {
    private int id;
    private final IdOption option;

    public enum IdOption {
        ORDER("config/orderId.conf" , 146957), PRODUCT("config/productId.conf" , 645733);
        public final String strpath;
        public final int default_id;

        IdOption(String strpath , int default_id) {
            this.strpath = strpath;
            this.default_id = default_id;
        }
        public String getStrPath(){
            return strpath;
        }
        public int getDefaultId(){
            return default_id;
        }
    }

    public IdGenerator(IdOption option) {
        this.option = option;

    }
    private void getIdFromFile() throws IOException , NumberFormatException{
        Path path = Paths.get(option.getStrPath());
        if(Files.exists(path)){
            this.id = Integer.parseInt(Files.readString(path));
            Files.writeString(path , Integer.toString(id + 1));
        }
        else {
            Files.createDirectories(path.getParent());
            Files.writeString(path , Integer.toString(option.getDefaultId()));
            this.id = option.getDefaultId();
        }
    }
    public int getId() throws  IOException , NumberFormatException{
        getIdFromFile();
        return id;
    }
}
