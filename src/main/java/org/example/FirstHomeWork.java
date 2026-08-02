package org.example;

import java.util.ArrayList;
import java.util.List;

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
    public static String blastOff(int Start){
        String result = "";
        for (int i = Start; i > 0; i--) {
            result += i + " ";
        }
        return result + "Поехали!";
    }
    public static int sumToN(int n){
        int result = 0;
        for (int i = n; i > 0; i--) {
            result += i;
        }
        return result;
    }
    public static boolean hasBug(String[] messages){
        boolean result = false;
        for (int i = 0; i < messages.length; i++) {
            System.out.println("Элемент " + i + ": '" + messages[i] + "'");
            if ("Bug".equalsIgnoreCase(messages[i])){
                System.out.println("Найдено совпадение!");
                 result = true;
            }
        }
        return result;
    }
    public static String getEvenInRange(int start, int end){
        String result = "";
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0){
                if (!result.isEmpty()){
                    result += " ";
                }
                result += i;
            }
        }
        return result;
    }
    public static int findMax(int[] arr){
        int result = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] > result){
                result = arr[i];
            }
        }
        return result;
    }
    public static String[] reverse(String[] arr){
        String[] result = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[arr.length - 1 - i];
        }
        return result;
    }
    public static int calcAverage(List<Integer> list){
        Integer result = 0;
        for (int i = 0; i < list.size(); i++) {
           result += list.get(i);
        }
        return result/ list.size();
    }
    public static List<String> removeSpecificName(List<String> list, String nameToRemove){
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i);
            if (!name.equals(nameToRemove)){
                result.add(name);
            }
        }
        return result;
    }
}

