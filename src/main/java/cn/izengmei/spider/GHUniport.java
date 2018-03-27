package cn.izengmei.spider;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;


public class GHUniport extends BreadthCrawler {
    /**
     * 构造一个基于伯克利DB的爬虫
     * 伯克利DB文件夹为crawlPath，crawlPath中维护了历史URL等信息
     * 不同任务不要使用相同的crawlPath
     * 两个使用相同crawlPath的爬虫并行爬取会产生错误
     *
     * @param crawlPath 伯克利DB使用的文件夹
     * @param autoParse 是否根据设置的正则自动探测新URL
     */
    public GHUniport(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        this.addSeed("http://www.cazy.org/GH11_characterized.html#pagination_FUNC");
        this.addSeed("http://www.cazy.org/GH11_characterized.html?debut_FUNC=100#pagination_FUNC");
        this.addSeed("http://www.cazy.org/GH11_characterized.html?debut_FUNC=200#pagination_FUNC");
    }

    public void visit(Page page, CrawlDatums next) {


        //正则表达式匹配出链接
        String pattern = ".*uniprot.*";
        //分析DOM树
        Document document = page.doc();
        Elements elements = document.getElementsByTag("tbody");
        Elements eChild = elements.select("a[target]");

        for (Element e:eChild) {
            String href = e.attr("href");
            boolean isMach = Pattern.matches(pattern,href);
            if (isMach) {
                String GanBank = href.substring(31,href.length());
//                System.out.println(href);
//                System.out.println(GanBank);
                try {
                    String DBZBank = getBankByGen(GanBank);
                    writeFile("GH11-2.txt",GanBank+","+DBZBank);
                    System.out.println(GanBank+","+DBZBank+"\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }


        System.out.println("*************");
    }

    public static void main(String[] args) throws Exception {
        GHUniport gh11 = new GHUniport("crawl1",false);
        gh11.setThreads(50);
        gh11.start(1);

    }


    /**
     * 通过基因序列获取蛋白质序列
     * @param GenBank
     * @return
     */
    public static String getBankByGen(String GenBank) throws IOException {

        String url2 = "http://www.uniprot.org/uniprot/"
                +GenBank
                +".fasta";
        String content = Jsoup.connect(url2).get().body().text();
        String[] arr = content.split(" ");
        String pattern = "^[A-Z]*";

        String result = "";
        for (String s:arr) {
            if (Pattern.matches(pattern,s)) {
                result += s;
            }
        }
        return result;
    }


    /**
     * 写文件
     * @param file
     * @param conent
     */
    public static void writeFile(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent+"\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
