package com.askia.coremodel.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Utils {

    //解析方法
    public static String convertText(String text) {
        if (text != null) {
            //将string文本转换成html文本
            Document doc = Jsoup.parse(text);
            //获得所有p标签
            Elements links = doc.getElementsByTag("p");
            //实例化stringbuffer
            StringBuffer buffer = new StringBuffer();
            for (Element link : links) {
                //将文本提前出来
                buffer.append(link.text().trim() + "\n");
            }
            return buffer.toString().trim();
        }
        return null;
    }
}
