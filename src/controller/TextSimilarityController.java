package controller;

import model.TextSimilarityModel;
import view.TextSimilarityView;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextSimilarityController {

    private final TextSimilarityModel model;
    private final TextSimilarityView view;

    public TextSimilarityController(TextSimilarityModel model, TextSimilarityView view) {
        this.model = model;
        this.view = view;

        initListeners();
    }

    private void initListeners() {
        view.addLoadBtn1Listener(e -> loadTextFromFile(1));
        view.addLoadBtn2Listener(e -> loadTextFromFile(2));
        
        view.addSampleBtn1Listener(e -> view.setText1(getSampleText(1)));
        view.addSampleBtn2Listener(e -> view.setText2(getSampleText(2)));
        
        view.addClearBtnListener(e -> clearAll());
        view.addCompareBtnListener(e -> compareTexts());
    }

    // Opens a file chooser and loads the selected text file into a text area.
    private void loadTextFromFile(int textId) {
        JFileChooser chooser = new JFileChooser();
        int choice = chooser.showOpenDialog(view);

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                String content = Files.readString(Path.of(file.getAbsolutePath()));
                if (textId == 1) {
                    view.setText1(content);
                } else {
                    view.setText2(content);
                }
                view.updateStatusLabel("Loaded: " + file.getName(), true);
            } catch (IOException ex) {
                view.displayError("Failed to read file:\n" + ex.getMessage(), "Error");
            }
        }
    }

    // Clears all fields and resets the screen.
    private void clearAll() {
        view.setText1("");
        view.setText2("");
        view.setResultText("");
        view.resetLabels();
    }

    // Main comparison flow:
    // 1. Read inputs
    // 2. Preprocess texts
    // 3. Run LCS
    // 4. Compute similarity
    // 5. Show output
    private void compareTexts() {
        String raw1 = view.getText1();
        String raw2 = view.getText2();

        // Both texts must contain some content.
        if (raw1.isEmpty() || raw2.isEmpty()) {
            view.displayWarning("Please enter or load both texts before comparing.", "Missing Input");
            return;
        }

        try {
            TextSimilarityModel.ComparisonResult result = model.compare(raw1, raw2);

            view.updateScoreLabel(result.similarity);
            view.updateStatusLabelAfterCompare(result.similarity);

            // Build readable result text.
            StringBuilder output = new StringBuilder();
            output.append("Text 1 tokens: ").append(result.tokens1Length).append('\n');
            output.append("Text 2 tokens: ").append(result.tokens2Length).append('\n');
            output.append("LCS length: ").append(result.lcsResult.length).append('\n');
            output.append("Similarity formula: LCS / max(length1, length2) * 100\n\n");
            output.append("Matched sequence:\n");

            if (result.lcsResult.sequence.isEmpty()) {
                output.append("(No common sequence found)");
            } else {
                output.append(String.join(" ", result.lcsResult.sequence));
            }

            output.append("\n\nCommon subsequence count: ").append(result.lcsResult.sequence.size());
            view.setResultText(output.toString());

        } catch (Exception e) {
            view.displayWarning(e.getMessage(), "Invalid Input");
        }
    }

    // Sample text used for testing without file input.
    private String getSampleText(int id) {
        if (id == 1) {
            return """
                    Artificial intelligence is changing the way people work and learn.
                    It helps automate tasks, analyze data, and improve decision making.
                    Many students use technology every day for research and communication.
                    """;
        }
        return """
                Artificial intelligence is changing the way people study and work.
                It helps automate tasks, process data, and support better decisions.
                Many students use technology daily for research, writing, and communication.
                """;
    }
}
