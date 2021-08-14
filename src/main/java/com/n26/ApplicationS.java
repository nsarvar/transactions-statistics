package com.n26;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class ApplicationS {

    public static void main(String... args) {
        int N = 101;
        int max = 0;
        int counter = 0;

        while (N > 0) {
            if ((N & 1) == 1) {
                max = Math.max(max, counter);
                counter = 0;
            } else if(counter > 0) {
                counter++;
            }
            N >>= 1;
        }
        System.out.println(max);
/**
 * while (N != 0) {
 *             if ((N & 1) == 0) {
 *                 cnt++;
 *                 N >>= 1;
 * //                maxm = Math.max(maxm, cnt);
 *             } else if(cnt > 0) {
 *                 if (cnt > maxm)
 *                     maxm = cnt;
 *                 cnt = 0;
 *                 N >>= 1;
 *             }
 *         }
 *         System.out.println(maxm);
 */
        //            if ((N & 1) == 0) {
//                cnt++;
//                N >>= 1;
////                maxm = Math.max(maxm, cnt);
//            } else{
//                if (cnt > maxm)
//                    maxm = cnt;
//                cnt = 0;
//                N >>= 1;
//            }
    }

}
