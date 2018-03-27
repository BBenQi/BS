package cn.izengmei.spider;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemoveDuplicate {
    public static void main(String[] args) throws IOException {
        File file = new File("GH11.txt");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String,String> map1 = new HashMap<String, String>(227);
        String tempString = "";
        while ((tempString = bufferedReader.readLine())!=null) {
            String[] arr = tempString.split(",");
            map1.put(arr[1],arr[0]);
        }
        System.out.println(map1);



        File file1 = new File("GH11-2.txt");
        BufferedReader bufferedReader1 = new BufferedReader(new FileReader(file1));
        Map<String,String> map2 = new HashMap<String, String>(310);
        while ((tempString = bufferedReader1.readLine())!=null) {
            String[] arr = tempString.split(",");
            map2.put(arr[1],arr[0]);
        }
        System.out.println(map2);


        Set<String> set1 = map1.keySet();
        Set<String> set2 = map2.keySet();
        Set<String> resultSet = new HashSet<String>();
        resultSet.addAll(set1);
        resultSet.addAll(set2);

        for (String seq:resultSet) {
            if (set2.contains(seq)) {
                writeFile("OKFile.txt",map2.get(seq)+","+seq);
            }else {
                writeFile("OKFile.txt",map1.get(seq)+","+seq);
            }
        }
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
