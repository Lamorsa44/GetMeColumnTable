package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Web
        System.out.println("Paste URL of web");
        Document document = getDocument(sc.next());
        // Web | Table
        var tables = document.getElementsByTag("table");
        tables.forEach(table -> {
            System.out.println(Counter.getCountAndIncrement() +
                    " :Table with rows: " + table.select("tr").first().text());
        });
        System.out.println("Select table index from 0 to n");
        var gudTable = tables.get(sc.nextInt());
        // Table | Column
        Counter.reset();
        String[] columns = gudTable.select("tr").first().stream().skip(1)
                .map(Element::text).toArray(String[]::new);
        System.out.println("Selected column: ");
        for (String s : columns) {
            System.out.printf("%d: %s\n", Counter.getCountAndIncrement(), s);
        }
        Counter.reset();

        System.out.println("Select column to fetch data from 0 to n");
        int column = sc.nextInt();
        // Column | Rows
        var rows = gudTable.select("tr");
        final HashSet<String> set = new HashSet<>();
        rows.forEach(row -> set.add(row.child(column).text()));
        set.forEach(x -> System.out.println(Counter.getCountAndIncrement() + ": " + x));

        //Maybe get all matching results and display? different pattern selection?
        sc.nextLine();
        System.out.println("Enter text you want to search, when you're done use ctrl + D or C idk");
        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            Function<String, String> patternFunction = s -> "(?i)" + s + ".*";
            if (set.stream().anyMatch(text -> text.matches(patternFunction.apply(word)))) {
                Counter.reset();
                set.stream().filter(text -> text.matches(patternFunction.apply(word)))
                        .forEach(s -> System.out.println(Counter.getCountAndIncrement() + ": " + s));
            } else {
                System.out.println("None found");
            }
        }
    }

    private static Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to web", e);
        }
    }

    static class Counter {
        static int count = 0;

        static void reset() {
            count = 0;
        }

        static public int getCountAndIncrement() {
            return count++;
        }
    }
}