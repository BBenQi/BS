package cn.izengmei.test2;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        BigInteger bigInteger = new BigInteger(sc.next());

        for (int i=1;i<N;i++) {
            bigInteger = bigInteger.add(new BigInteger(sc.next()));
        }
        System.out.println(bigInteger.toString());
    }
}
