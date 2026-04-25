package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class TextSimilarityView extends JFrame {

    // Dark theme colors used throughout the app.
    private static final Color BG = new Color(15, 18, 28);
    private static final Color CARD = new Color(24, 28, 40);
    private static final Color CARD_2 = new Color(30, 35, 50);
    private static final Color BORDER = new Color(60, 70, 95);
    private static final Color TEXT = new Color(235, 238, 245);
    private static final Color MUTED = new Color(160, 168, 185);
    private static final Color ACCENT = new Color(92, 124, 250);
    private static final Color ACCENT_HOVER = new Color(112, 144, 255);
    private static final Color DANGER = new Color(232, 85, 99);
    private static final Color SUCCESS = new Color(68, 194, 132);

    // Input areas for both texts.
    private final JTextArea textArea1 = new JTextArea();
    private final JTextArea textArea2 = new JTextArea();
    
    // Output area used to show result details.
    private final JTextArea resultArea = new JTextArea();

    // Labels for similarity score and status.
    private final JLabel scoreLabel = new JLabel("Similarity: 0.00%");
    private final JLabel statusLabel = new JLabel("Ready");

    private JButton loadButton1;
    private JButton sampleButton1;
    private JButton loadButton2;
    private JButton sampleButton2;
    private JButton clearButton;
    private JButton compareButton;

    public TextSimilarityView() {
        // Apply theme before creating UI.
        applyDarkTheme();

        setTitle("Text Similarity Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 820));
        setLocationRelativeTo(null);
        setResizable(true);

        // Root container that holds all major panels.
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createCenterPanel(), BorderLayout.CENTER);
        root.add(createBottomPanel(), BorderLayout.SOUTH);
    }

    // Applies Nimbus look and feel and keeps the colors consistent.
    private void applyDarkTheme() {
        UIManager.put("control", CARD);
        UIManager.put("info", CARD);
        UIManager.put("nimbusBase", BG);
        UIManager.put("nimbusBlueGrey", CARD);
        UIManager.put("nimbusLightBackground", CARD_2);
        UIManager.put("text", TEXT);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Button.foreground", TEXT);
        UIManager.put("TextArea.foreground", TEXT);
        UIManager.put("TextArea.background", CARD_2);
        UIManager.put("Panel.background", BG);
        UIManager.put("ScrollPane.background", BG);
        UIManager.put("ScrollBar.track", BG);
        UIManager.put("ScrollBar.thumb", BORDER);
        UIManager.put("OptionPane.background", BG);
        UIManager.put("OptionPane.messageForeground", TEXT);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    // Builds the top header section with title and similarity score.
    private JComponent createHeader() {
        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setOpaque(false);

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Text Similarity Checker");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Dark UI • Java Swing • LCS-based comparison");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(subtitle);

        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        scoreLabel.setForeground(SUCCESS);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(titleBlock, BorderLayout.WEST);
        header.add(scoreLabel, BorderLayout.EAST);
        return header;
    }

    // Builds the middle section with the two input cards.
    private JComponent createCenterPanel() {
        JPanel center = new JPanel(new GridLayout(1, 2, 16, 16));
        center.setOpaque(false);
        
        loadButton1 = new AccentButton("Load File", ACCENT, ACCENT_HOVER);
        sampleButton1 = new AccentButton("Load Sample", CARD_2, new Color(52, 58, 78));
        center.add(createInputCard("Text 1", textArea1, loadButton1, sampleButton1));

        loadButton2 = new AccentButton("Load File", ACCENT, ACCENT_HOVER);
        sampleButton2 = new AccentButton("Load Sample", CARD_2, new Color(52, 58, 78));
        center.add(createInputCard("Text 2", textArea2, loadButton2, sampleButton2));
        
        return center;
    }

    // Creates one input card with a text box, file button, and sample button.
    private JComponent createInputCard(String title, JTextArea area, JButton loadBtn, JButton sampleBtn) {
        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout(12, 12));
        card.setBackground(CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(TEXT);

        JLabel hint = new JLabel("Paste text or load a file");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hint.setForeground(MUTED);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(label, BorderLayout.WEST);
        top.add(hint, BorderLayout.EAST);

        // Apply shared text area styling.
        styleTextArea(area);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(new LineBorder(BORDER, 1, true));
        scroll.getViewport().setBackground(CARD_2);
        scroll.setBackground(CARD_2);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getHorizontalScrollBar().setUnitIncrement(14);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(loadBtn);
        actions.add(sampleBtn);

        card.add(top, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    // Builds the bottom section with the result box and action buttons.
    private JComponent createBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(12, 12));
        bottom.setOpaque(false);

        CardPanel resultCard = new CardPanel();
        resultCard.setLayout(new BorderLayout(10, 10));
        resultCard.setBackground(CARD);
        resultCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel resultTitle = new JLabel("Result / Matched Sequence");
        resultTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        resultTitle.setForeground(TEXT);

        // Result area is read-only because it only displays output.
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        resultArea.setForeground(TEXT);
        resultArea.setBackground(CARD_2);
        resultArea.setCaretColor(TEXT);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        resultArea.setSelectionColor(new Color(90, 110, 180));
        resultArea.setSelectedTextColor(Color.WHITE);

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(new LineBorder(BORDER, 1, true));
        resultScroll.getViewport().setBackground(CARD_2);
        resultScroll.setBackground(CARD_2);
        resultScroll.getVerticalScrollBar().setUnitIncrement(14);

        JPanel resultTop = new JPanel(new BorderLayout());
        resultTop.setOpaque(false);
        resultTop.add(resultTitle, BorderLayout.WEST);
        resultTop.add(statusLabel, BorderLayout.EAST);

        resultCard.add(resultTop, BorderLayout.NORTH);
        resultCard.add(resultScroll, BorderLayout.CENTER);

        // Buttons for clearing and comparing.
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        clearButton = new AccentButton("Clear", DANGER, new Color(255, 106, 122));
        compareButton = new AccentButton("Compare", SUCCESS, new Color(88, 214, 155));

        actions.add(clearButton);
        actions.add(compareButton);

        bottom.add(resultCard, BorderLayout.CENTER);
        bottom.add(actions, BorderLayout.SOUTH);
        return bottom;
    }

    // Styles a text area so it matches the rest of the UI.
    private void styleTextArea(JTextArea area) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setForeground(TEXT);
        area.setBackground(CARD_2);
        area.setCaretColor(TEXT);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        area.setSelectionColor(new Color(90, 110, 180));
        area.setSelectedTextColor(Color.WHITE);
    }

    public void displayError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void displayWarning(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public void addLoadBtn1Listener(ActionListener listener) {
        loadButton1.addActionListener(listener);
    }

    public void addLoadBtn2Listener(ActionListener listener) {
        loadButton2.addActionListener(listener);
    }

    public void addSampleBtn1Listener(ActionListener listener) {
        sampleButton1.addActionListener(listener);
    }

    public void addSampleBtn2Listener(ActionListener listener) {
        sampleButton2.addActionListener(listener);
    }

    public void addClearBtnListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    public void addCompareBtnListener(ActionListener listener) {
        compareButton.addActionListener(listener);
    }

    public String getText1() {
        return textArea1.getText().trim();
    }

    public String getText2() {
        return textArea2.getText().trim();
    }

    public void setText1(String text) {
        textArea1.setText(text);
    }

    public void setText2(String text) {
        textArea2.setText(text);
    }

    public void setResultText(String text) {
        resultArea.setText(text);
        resultArea.setCaretPosition(0);
    }

    public void updateScoreLabel(double similarity) {
        scoreLabel.setText(String.format("Similarity: %.2f%%", similarity));
        scoreLabel.setForeground(similarity >= 70 ? SUCCESS : ACCENT);
    }

    public void updateStatusLabel(String text, boolean isSuccessOrWarning) {
        statusLabel.setText(text);
        statusLabel.setForeground(isSuccessOrWarning ? SUCCESS : MUTED);
    }
    
    public void updateStatusLabelAfterCompare(double similarity) {
        statusLabel.setText(similarity >= 70 ? "High similarity detected" : "Comparison complete");
        statusLabel.setForeground(similarity >= 70 ? DANGER : MUTED);
    }

    public void resetLabels() {
        scoreLabel.setText("Similarity: 0.00%");
        scoreLabel.setForeground(SUCCESS);
        statusLabel.setText("Ready");
        statusLabel.setForeground(MUTED);
    }

    // Rounded panel used to make the UI look like cards.
    private static class CardPanel extends JPanel {
        CardPanel() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Custom button with rounded shape and hover effect..
    private static class AccentButton extends JButton {
        private final Color normal;
        private final Color hover;
        private boolean hovered = false;

        AccentButton(String text, Color normal, Color hover) {
            super(text);
            this.normal = normal;
            this.hover = hover;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 16, 10, 16));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        // Renderer
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color fill = hovered ? hover : normal;
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            g2.setColor(new Color(255, 255, 255, 35));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();

            super.paintComponent(g);
        }
    }
}
