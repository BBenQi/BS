package cn.izengmei.spider2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Ncbi{
    /**
     * 根据关键词，查找论文
     * @return Map<String,String> </蛋白质ID,论文链接>
     * */
    public static ConcurrentHashMap<String,List<String>> downLoad(Map<String, String> map) throws IOException {
        ConcurrentHashMap<String,List<String>> result = new ConcurrentHashMap<String,List<String>>();

        String baseURL = "https://www.ncbi.nlm.nih.gov/pubmed/?term=";
        Set<String> keySet = map.keySet();
        for (String keyWord:keySet) {
            String url = baseURL + map.get(keyWord).replace(' ','+');
            List<String> list = new LinkedList<String>();
            try {
                Document document = Jsoup.connect(url).get();
//                System.out.println(document.select("p[class=\"title\"]").size());
                Elements elements = document.select("p[class=\"title\"]");
                for (Element e:elements) {
                   list.add("https://www.ncbi.nlm.nih.gov" + e.child(0).attr("href"));
                    System.out.println("找到一篇文章");
                }
                result.put(keyWord,list);
            } catch (Exception e) {
                System.out.println("请求超时");
                continue;
            }

        }
        return result;
    }

    static class PrasePaper extends Thread {
        private ConcurrentHashMap<String,List<String>> map;
        public PrasePaper(ConcurrentHashMap<String,List<String>> map) {
            this.map = map;
        }
        @Override
        public synchronized void run() {
            try {
                prasePaper(map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析论文，并获得PH值
     * */
    public static  void prasePaper(ConcurrentHashMap<String,List<String>> map) throws IOException {
        List<String> paperLines = new LinkedList<String>();
        Set<String> IDs = map.keySet();
        for (String ID:IDs) {
            List<String> list = map.get(ID);
            map.remove(ID);
            for (String url:list) {
                try {
                    Document document = Jsoup.connect(url).get();
                    Element element = document.select("div[class=\"abstr\"]").first();
                    System.out.println(element.text());
                    String text = element.text();
                    paperLines = getPHT(text);
                    System.out.println("********");
                    System.out.println(ID);
                    System.out.println(paperLines.toString());
                    System.out.println("*************");
                    String content = "";
                    for (String s:paperLines) {
                        FileTool.writeFile("PH.csv",ID+","+url+","+s);
                    }
                } catch (Exception e) {
                    System.out.println("请求超时");
                    continue;
                }
            }
        }
    }

    /**
     * 从论文中获取PH和温度
     * */
    public static List<String> getPHT(String paper) {
        List<String> result = new LinkedList<String>();
        String[] sarr = paper.split("\\.\\W");
        for (String s:sarr) {
            if (s.matches(".*pH.*")||s.matches(".*°C.*")) {
                System.out.println(s);
                result.add(s);
            }
        }
        return result;
    }
    public static void main(String[] args) throws IOException {
        ConcurrentHashMap<String,List<String>> concurrentHashMap = downLoad(Main.getKeyWords());
        PrasePaper p1 = new PrasePaper(concurrentHashMap);
        PrasePaper p2 = new PrasePaper(concurrentHashMap);
        PrasePaper p3 = new PrasePaper(concurrentHashMap);
        PrasePaper p4 = new PrasePaper(concurrentHashMap);
        PrasePaper p5 = new PrasePaper(concurrentHashMap);
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
//        String s = "We here describe a unique β-D-glucosidase (BGL; Blon_0625) derived from Bifidobacterium longum subsp. infantis ATCC 15697. The Blon_0625 gene was expressed by recombinant Escherichia coli. Purified recombinant Blon_0625 retains hydrolyzing activity against both p-nitrophenyl-β-D-glucopyranoside (pNPG; 17.3±0.24Umg(-1)) and p-nitrophenyl-β-D-xylopyranoside (pNPX; 16.7±0.32Umg(-1)) at pH 6.0, 30°C. To best of our knowledge, no previously described BGL retains the same level of both pNPGase and pNPXase activity. Furthermore, Blon_0625 also retains the activity against 4-nitrophenyl-α-l-arabinofranoside (pNPAf; 5.6±0.09Umg(-1)). In addition, the results of the degradation of phosphoric acid swollen cellulose (PASC) or xylan using endoglucanase from Thermobifida fusca YX (Tfu_0901) or xylanase from Kitasatospora setae KM-6054 (KSE_59480) show that Blon_0625 acts as a BGL and as a β-D-xylosidase (XYL) for hydrolyzing oligosaccharides. These results clearly indicate that Blon_0625 is a multi-functional glycoside hydrolase which retains the activity of BGL, XYL, and also α-l-arabinofuranosidase. Therefore, the utilization of multi-functional Blon_0625 may contribute to facilitating the efficient degradation of lignocellulosic materials and help enhance bioconversion processes.";
//        getPHT(s);

    }
}
