package com.example.kakurogamelatestversion.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeTemplate {

    public static List<String[][]> getTemplates() {
        List<String[][]> templates = new ArrayList<>();

        templates.add(new String[][]{
                {"/", "⬍5", "⬍7"},
                {"➞12", "", ""},
                {"/", "/", "/"}
        });

        templates.add(new String[][]{
                {"/", "⬍4", "⬍8"},
                {"➞12", "", ""},
                {"➞6", "", ""}
        });

        templates.add(new String[][]{
                {"/", "⬍10", "⬍3"},
                {"➞13", "", ""},
                {"➞0", "", ""}
        });

        templates.add(new String[][]{
                {"/", "⬍9", "⬍6"},
                {"➞15", "", ""},
                {"➞0", "", ""}
        });

        templates.add(new String[][]{
                {"/", "⬍6", "⬍5"},
                {"➞11", "", ""},
                {"➞0", "", ""}
        });

        templates.add(new String[][]{
                {"/", "⬍4", "⬍6"},
                {"➞10", "", ""},
                {"➞0", "", ""}
        });

        templates.add(new String[][]{
                {"/", "⬍7", "⬍5"},
                {"➞12", "", ""},
                {"➞0", "", ""}
        });

        templates.add(new String[][]{
                {"/", "⬍6", "⬍6"},
                {"➞12", "", ""},
                {"➞0", "", ""}
        });

        templates.add(new String[][]{
                {"/", "⬍3", "⬍7"},
                {"➞10", "", ""},
                {"➞0", "", ""}
        });

        templates.add(new String[][]{
                {"/", "⬍8", "⬍2"},
                {"➞10", "", ""},
                {"➞0", "", ""}
        });

        return templates;
    }

    public static List<List<Integer>> getSolutions() {
        List<List<Integer>> solutions = new ArrayList<>();

        solutions.add(Arrays.asList(0, 0, 0, 5, 7, 0, 0, 0, 0));
        solutions.add(Arrays.asList(0, 0, 0, 4, 8, 0, 2, 4, 0));
        solutions.add(Arrays.asList(0, 0, 0, 10, 3, 0, 0, 0, 0));
        solutions.add(Arrays.asList(0, 0, 0, 9, 6, 0, 0, 0, 0));
        solutions.add(Arrays.asList(0, 0, 0, 6, 5, 0, 0, 0, 0));
        solutions.add(Arrays.asList(0, 0, 0, 4, 6, 0, 0, 0, 0));
        solutions.add(Arrays.asList(0, 0, 0, 7, 5, 0, 0, 0, 0));
        solutions.add(Arrays.asList(0, 0, 0, 6, 6, 0, 0, 0, 0));
        solutions.add(Arrays.asList(0, 0, 0, 3, 7, 0, 0, 0, 0));
        solutions.add(Arrays.asList(0, 0, 0, 8, 2, 0, 0, 0, 0));

        return solutions;
    }
}
