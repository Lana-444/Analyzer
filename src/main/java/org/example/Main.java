package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Main {

    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void main(String[] args) {

        Thread generatorThread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 10000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        });

        Thread counterThreadA = new Thread(() -> {
            int maxCountA = 0;
            String maxTextA = "";
            try {
                while (true) {
                    String text = queueA.take();
                    int countA = 0;
                    for (char a : text.toCharArray()) {
                        if (a == 'a') {
                            countA++;
                        }
                    }if (countA > maxCountA) {
                        maxCountA = countA;
                        maxTextA = text;
                    }

                }
            } catch (InterruptedException e) {
                System.out.println("Текст: " + maxTextA + " - в котором содержится максимальное количество символов 'a': "  + maxCountA);
            }
        });
        Thread counterThreadB = new Thread(() -> {
            int maxCountB = 0;
            String maxTextB = "";
            try {
                while (true) {
                    String text = queueB.take();
                    int countB = 0;
                    for (char b : text.toCharArray()) {
                        if (b == 'b') {
                            countB++;
                        }
                        if (countB > maxCountB) {
                            maxCountB = countB;
                            maxTextB = text;
                        }
                    }System.out.println("Текст: " + maxTextB + " - в котором содержится максимальное количество символов 'b': "  + maxCountB);
                }
            } catch (InterruptedException e) {

            }
        });


        Thread counterThreadC = new Thread(() -> {
            int maxCountC = 0;
            String maxTextC = "";
            try {
                while (true) {
                    String text = queueC.take();
                    int countC = 0;
                    for (char c : text.toCharArray()) {
                        if (c == 'c') {
                            countC++;
                        }
                        if (countC > maxCountC) {
                            maxCountC = countC;
                            maxTextC = text;
                        }
                    }System.out.println("Текст: " + maxTextC + " - в котором содержится максимальное количество символов 'с': " + maxCountC);
                }
            } catch (InterruptedException e) {
            }
        });
        generatorThread.start();
        counterThreadA.start();
        counterThreadB.start();
        counterThreadC.start();


        try {
            generatorThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        counterThreadA.interrupt();
        counterThreadB.interrupt();
        counterThreadC.interrupt();
    }
}
