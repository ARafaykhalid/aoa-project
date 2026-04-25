// packages import statements
import controller.TextSimilarityController;
import model.TextSimilarityModel;
import view.TextSimilarityView;
// Java Swing imports
import javax.swing.SwingUtilities;

public class TextSimilarityCheckerApp {
    // Main method to launch the application.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextSimilarityModel model = new TextSimilarityModel();
            TextSimilarityView view = new TextSimilarityView();
            new TextSimilarityController(model, view);

            view.setVisible(true);
        });
    }
}
