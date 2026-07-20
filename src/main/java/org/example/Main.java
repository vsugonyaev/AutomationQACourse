package org.example;

import java.util.Arrays;
import java.util.List;

import static org.example.FirstHomeWork.blastOff;
import static org.example.FirstHomeWork.sumToN;
import static org.example.FirstHomeWork.hasBug;
import static org.example.FirstHomeWork.getEvenInRange;
import static org.example.FirstHomeWork.findMax;
import static org.example.FirstHomeWork.reverse;
import static org.example.FirstHomeWork.calcAverage;
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args){
        List<Integer> list1 = Arrays.asList(2, 4, 6, 8);
        System.out.println("Среднее для [2,4,6,8]: " + calcAverage(list1));
    }
}