package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    static int sentences = 0;
    static int words = 0;
    static int characters;
    static int syllables = 0;
    static int polySyllables = 0;
    static int ageAll = 0;
    static int amountOfAlgorithms = 0;

    public static void main(String[] args) {
       readFromFile(args[0]);
    }

    public static void readFromFile(String filename) {
        Scanner scanner = new Scanner(System.in);

        try {
            String text = new String(Files.readAllBytes(Paths.get(filename)));

            sentences = text.split("[!.?]").length;
            words = text.split(" ").length;
            characters = text.replaceAll("\\s+", "").length();

            countSyllablesAndPolysyllables(text.replaceAll("[,:!.?]", ""));

            output();

            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            String command = scanner.next();

            chooseAlgorithm(command);

            System.out.println("\nThis text should be understood in average by " + (double)ageAll / amountOfAlgorithms + " year olds.");

        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
    }

    public static void countSyllablesAndPolysyllables(String text) {
        int syllablesInWord;
        boolean previousIsVowel = false;
        String letter;
        for (String word : text.split("\\s+")) {
            syllablesInWord = 0;
            for (int i = 0; i < word.length(); i++) {

                letter = String.valueOf(word.charAt(i));

                if (letter.matches("[aeiouyAEIOUY]")) {
                    if (!previousIsVowel) {

                        if (i != word.length() - 1 || word.charAt(i) != 'e') {
                            syllablesInWord++;
                        }

                        previousIsVowel = true;
                    }
                } else {
                    previousIsVowel = false;
                }
            }

            if (syllablesInWord == 0) {
                syllablesInWord = syllablesInWord + 1;
            }

            if (syllablesInWord > 2) {
                polySyllables++;
            }

            syllables += syllablesInWord;
            previousIsVowel = false;
        }


    }

    public static void automatedReadabilityIndex() {

        double score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;

        System.out.printf("Automated Readability Index: %.2f", score);
        getAgeFromScore(score);

        amountOfAlgorithms++;
    }

    public static void fleschKincaidtests() {

        double score = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;

        System.out.printf("Flesch–Kincaid readability tests: %.2f", score);
        getAgeFromScore(score);

        amountOfAlgorithms++;
    }

    public static void smogIndex() {

        double score = 1.043 * Math.sqrt((double) polySyllables * 30 / sentences) + 3.1291;

        System.out.printf("Simple Measure of Gobbledygook: %.2f", score);
        getAgeFromScore(score);

        amountOfAlgorithms++;
    }

    public static void colemanLiauIndex() {

        double l = (double) characters / words * 100;
        double s = (double) sentences / words * 100;

        double score = 0.0588 * l - 0.296 * s - 15.8;

        System.out.printf("Coleman–Liau index: %.2f", score);
        getAgeFromScore(score);

        amountOfAlgorithms++;
    }

    public static void output() {
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polySyllables);
    }

    public static void getAgeFromScore(double score) {
        int age = 0;
        int roundScore = (int) Math.round(score);

        if (1 <= roundScore && roundScore <= 2) {
            age = roundScore + 5;
        } else if (3 <= roundScore && roundScore <= 12) {
            age = roundScore + 6;
        } else if (roundScore == 13 || roundScore == 14) {
            age = 24;
        }

        ageAll += age;
        System.out.println(" (about "+ age + " year olds) .");
    }

    public static void chooseAlgorithm(String command) {
        switch (command) {
            case "ARI" : automatedReadabilityIndex(); break;
            case "FK" : fleschKincaidtests(); break;
            case "SMOG" : smogIndex(); break;
            case "CL" : colemanLiauIndex(); break;
            case "all" :  {
                automatedReadabilityIndex();
                fleschKincaidtests();
                smogIndex();
                colemanLiauIndex();
                break;
            }
        }
    }

}
