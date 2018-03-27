package cn.izengmei.spider2;

import java.io.IOException;
import java.util.Map;

public interface BaseSpider {
    /**
     * 根据关键字下载论文到本地
     * */
    public void  downLoad(Map<String,String > map) throws IOException;
}
