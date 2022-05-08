package ru.netology.graphics;

import ru.netology.graphics.image.MyTextGraphicsConverter;
import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.server.GServer;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new MyTextGraphicsConverter();

        GServer server = new GServer(converter);
        server.start();

//        String url = "";
//        String imgTxt = converter.convert(url);
//        System.out.println(imgTxt);
    }
}
