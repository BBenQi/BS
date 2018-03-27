package cn.izengmei.spider2;
/**
 * 获取搜索查询所需要的关键字
 * */
import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static Map<String,String> getKeyWords() throws IOException {
        String[] url = {"http://www.cazy.org/GH11_characterized.html#pagination_FUNC"
        ,"http://www.cazy.org/GH11_characterized.html?debut_FUNC=100#pagination_FUNC"
        ,"http://www.cazy.org/GH11_characterized.html?debut_FUNC=200#pagination_FUNC"};
        Map<String,String> result = new HashMap<String, String>();


        for (String s:url) {
            Document document = Jsoup.connect(s).get();
//            System.out.println(document.text());
            Elements elements = document.select("tr[valign=\"top\"]");
            for (Element element:elements) {
                Elements children = element.children();
//                System.out.println(praseProteinName(children.get(0).text()));
//                System.out.println(children.get(2).text());

                if (children.get(4).text().equals(" ")) {
                    result.put(children.get(4).text(),praseProteinName(children.get(0).text())+" "+children.get(2).text());
                }else {
                    result.put(children.get(3).text(),praseProteinName(children.get(0).text())+" "+children.get(2).text());
                }
//                System.out.println(children.get(3).text());
//                System.out.println(children.get(4).text());
            }
        }
        return result;
    }
    /**
     * 获取括号中的内容
     * */
    public static String praseProteinName(String str) {
        String[] sarr = str.split("\\(|\\)");
        return sarr[sarr.length-1];
    }

    public static void main(String[] args) throws IOException {

        List<String> list = new LinkedList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println(list.toString());
//        System.out.println(getKeyWords().toString());
//        System.out.println(praseProteinName(" xylanase A (XynA)"));
    }
}
