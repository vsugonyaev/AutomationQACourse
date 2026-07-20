package org.example;

public class FirstHomeWork {
    public static boolean isEven(int n) {
        boolean result = (n % 2 == 0) ? true : false;
        return result;
    }

    public static String checkAccess(int age) {
        String result = (age > 18) ? "Allowed" : "Denied";
        return result;
    }
    public static boolean isPositive(int n){
        boolean result = (n >= 0) ? true : false;
        return result;
    }
    public static String getGrade(int Score){
        if (Score < 0 || Score > 100){
            return "Error";
        }
        if (Score <= 20) {
            return "E";
        }
        if (Score <=40) {
            return "D";
        }
        if (Score <= 60) {
            return "C";
        }
        if (Score <= 80) {
            return  "B";
        }
        return "A";
    }
}
