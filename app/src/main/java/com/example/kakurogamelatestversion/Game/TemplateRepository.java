package com.example.kakurogamelatestversion.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TemplateRepository {

    public static List<Template> getEasyTemplates() {
        List<Template> templates = new ArrayList<>();

        templates.add(createTemplate("easy", new String[][]{
                {"/", "⬍11", "⬍4", "/"},
                {"➞12", "", "", "/"},
                {"➞3", "", "", "/"},
                {"/", "/", "/", "/"}
        }));

        templates.add(createTemplate("easy", new String[][]{
                {"/", "⬍17", "⬍16", "/"},
                {"➞17", "", "", "/"},
                {"➞16", "", "", "/"},
                {"/", "/", "/", "/"}
        }));

        templates.add(createTemplate("easy", new String[][]{
                {"/", "⬍15", "⬍7", "/"},
                {"➞15", "", "", "/"},
                {"➞7", "", "", "/"},
                {"/", "/", "/", "/"}
        }));

        templates.add(createTemplate("easy", new String[][]{
                {"/", "⬍12", "⬍9", "/"},
                {"➞17", "", "", "/"},
                {"➞4", "", "", "/"},
                {"/", "/", "/", "/"}
        }));

        templates.add(createTemplate("easy", new String[][]{
                {"/", "⬍6", "⬍5", "/"},
                {"➞7", "", "", "/"},
                {"➞4", "", "", "/"},
                {"/", "/", "/", "/"}
        }));

        return templates;
    }

    public static List<Template> getMediumTemplates() {
        List<Template> templates = new ArrayList<>();

        templates.add(createTemplate("medium", new String[][]{
                {"/", "⬍20", "⬍15", "⬍10"},
                {"➞23", "", "", ""},
                {"➞15", "", "", ""},
                {"➞7", "", "", ""}
        }));

        templates.add(createTemplate("medium", new String[][]{
                {"/", "⬍21", "⬍11", "⬍10"},
                {"➞23", "", "", ""},
                {"➞12", "", "", ""},
                {"➞7", "", "", ""}
        }));

        templates.add(createTemplate("medium", new String[][]{
                {"/", "⬍23", "⬍13", "⬍9"},
                {"➞23", "", "", ""},
                {"➞12", "", "", ""},
                {"➞10", "", "", ""}
        }));

        templates.add(createTemplate("medium", new String[][]{
                {"/", "⬍24", "⬍17", "⬍7"},
                {"➞20", "", "", ""},
                {"➞18", "", "", ""},
                {"➞10", "", "", ""}
        }));

        templates.add(createTemplate("medium", new String[][]{
                {"/", "⬍20", "⬍15", "⬍10"},
                {"➞23", "", "", ""},
                {"➞15", "", "", ""},
                {"➞7", "", "", ""}
        }));

        return templates;
    }

    public static List<Template> getHardTemplates() {
        List<Template> templates = new ArrayList<>();

        templates.add(createTemplate("hard", new String[][]{
                {"/", "⬍20", "⬍11", "⬍7"},
                {"➞20", "", "", ""},
                {"➞12", "", "", ""},
                {"➞6", "", "", ""}
        }));


        templates.add(createTemplate("hard", new String[][]{
                {"/", "⬍23", "⬍15", "⬍10"},
                {"➞24", "", "", ""},
                {"➞14", "", "", ""},
                {"➞10", "", "", ""}
        }));

        templates.add(createTemplate("hard", new String[][]{
                {"/", "⬍23", "⬍20", "⬍7"},
                {"➞20", "", "", ""},
                {"➞19", "", "", ""},
                {"➞11", "", "", ""}
        }));

        templates.add(createTemplate("hard", new String[][]{
                {"/", "⬍24", "⬍17", "⬍7"},
                {"➞20", "", "", ""},
                {"➞18", "", "", ""},
                {"➞10", "", "", ""}
        }));

        templates.add(createTemplate("hard", new String[][]{
                {"/", "⬍21", "⬍20", "⬍10"},
                {"➞23", "", "", ""},
                {"➞19", "", "", ""},
                {"➞9", "", "", ""}
        }));

        return templates;
    }

    private static Template createTemplate(String difficulty, String[][] grid) {
        Template template = new Template();
        template.gridUid = UUID.randomUUID().toString();
        template.grid = grid;
        template.size = grid.length;
        template.isSolved = false;
        template.rowHints = new HashMap<>();
        template.colHints = new HashMap<>();
        template.cellStates = new Template.Cell[grid.length * grid.length];
        for (int i = 0; i < template.cellStates.length; i++) {
            template.cellStates[i] = new Template.Cell();
        }


        template.setDifficulty(difficulty);

        return template;
    }
}
