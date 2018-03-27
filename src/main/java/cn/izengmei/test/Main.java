package cn.izengmei.test;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        String[] arr = new String[N];
        String s = null;

        for (int i=0;i<N;i++) {
            System.out.println(getMaxUcode(sc.next()));
        }


        sc.close();
    }
    /**
     * 返回各字符串中Unicode值最大字符
     * */
    public static char getMaxUcode(String s) {
        int n=0;
        char temp = s.charAt(0);
        for (int i=0;i<s.length();i++) {
            if (n<(int)s.charAt(i)) {
                n = (int)s.charAt(i);
                temp = s.charAt(i);
            }
        }
        return temp;
    }
}
