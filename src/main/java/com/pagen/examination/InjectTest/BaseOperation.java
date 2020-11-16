package com.pagen.examination.InjectTest;

import java.util.Random;

/**
 * 基本处理类
 */
public class BaseOperation {

    /**
     * 产生一串随机字符串
     *
     * @param length ：字符串长度
     * @param level  ： 字符串等级（1：仅包含数字 | 2：仅包含小写字母 | 3：仅包含大写字母 | 4：仅含大小写字母 | 5：含字母和数字）
     * @return ：随机字符串
     */
    public static String getRandomString(int length, int level) {
        String str = null;
        switch (level) {
            case 1:
                str = "123456789";
                level = 9;
                break;
            case 2:
                str = "abcdefghijklmnopqrstuvwxyz";
                level = 26;
                break;
            case 3:
                str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                level = 26;
                break;
            case 4:
                str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                level = 52;
                break;
            case 5:
                str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                level = 62;
                break;
        }
        if (str == null)
            return null;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(random.nextInt(level)));
        }
        return sb.toString();
    }

    /**
     * 检测页面相似度算法
     */
    public Double Leven(String Str_1, String Str_2) {
        int Length1 = Str_1.length();
        int Length2 = Str_2.length();

        int Distance = 0;
        if (Length1 == 0) {
            Distance = Length2;
        }
        if (Length2 == 0) {
            Distance = Length1;
        }
        if (Length1 != 0 && Length2 != 0) {
            int[][] Distance_Matrix = new int[Length1 + 1][Length2 + 1];
            //编号
            int Bianhao = 0;
            for (int i = 0; i <= Length1; i++) {
                Distance_Matrix[i][0] = Bianhao;
                Bianhao++;
            }
            Bianhao = 0;
            for (int i = 0; i <= Length2; i++) {
                Distance_Matrix[0][i] = Bianhao;
                Bianhao++;
            }
            char[] Str_1_CharArray = Str_1.toCharArray();
            char[] Str_2_CharArray = Str_2.toCharArray();

            for (int i = 1; i <= Length1; i++) {
                for (int j = 1; j <= Length2; j++) {
                    if (Str_1_CharArray[i - 1] == Str_2_CharArray[j - 1]) {
                        Distance = 0;
                    } else {
                        Distance = 1;
                    }
                    int Temp1 = Distance_Matrix[i - 1][j] + 1;
                    int Temp2 = Distance_Matrix[i][j - 1] + 1;
                    int Temp3 = Distance_Matrix[i - 1][j - 1] + Distance;

                    Distance_Matrix[i][j] = Math.min(Temp1, Temp2);
                    Distance_Matrix[i][j] = Math.min(Distance_Matrix[i][j], Temp3);
                }
            }
            Distance = Distance_Matrix[Length1][Length2];
        }
        return 1 - 1.0 * Distance / (Math.max(Length1, Length2));
    }
}