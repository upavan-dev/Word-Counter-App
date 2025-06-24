package com.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WordCounterApp extends JFrame {
    // Add serialVersionUID to resolve serialization warning
    private static final long serialVersionUID = 1L;
    
    private JTextArea textArea;
    private JLabel wordCountLabel;
    private JLabel charCountLabel;
    private JLabel charNoSpaceLabel;
    private JLabel sentenceCountLabel;
    private JLabel paragraphCountLabel;
    private JTextPane previewPane;

    // Sample text for the "Load Sample Text" button
    private final String SAMPLE_TEXT = "The art of writing is the art of discovering what you believe. " +
            "Writing is not just about putting words on paper; it's about finding your voice and sharing " +
            "your unique perspective with the world.\n\n" +
            "Every great writer started as a beginner. They faced the blank page with the same uncertainty " +
            "and excitement that you might feel right now. The key is to start writing, even if you think " +
            "your first attempts aren't perfect.\n\n" +
            "Remember, writing is rewriting. Your first draft is just the beginning of your journey. " +
            "With each revision, your ideas become clearer, your arguments stronger, and your voice more authentic. " +
            "Don't be afraid to experiment with different styles and approaches.";

    public WordCounterApp() {
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupWindow();
    }

    private void initializeComponents() {
        // Initialize text area
        textArea = new JTextArea(15, 40);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize statistics labels
        wordCountLabel = createStatLabel("Words: 0");
        charCountLabel = createStatLabel("Characters: 0");
        charNoSpaceLabel = createStatLabel("Characters (no spaces): 0");
        sentenceCountLabel = createStatLabel("Sentences: 0");
        paragraphCountLabel = createStatLabel("Paragraphs: 0");

        // Initialize preview pane
        previewPane = new JTextPane();
        previewPane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        previewPane.setEditable(false);
        previewPane.setBackground(new Color(245, 245, 245));
        previewPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        previewPane.setText("Preview will appear here...");
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Text area with scroll pane
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(new TitledBorder("Enter Your Text"));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(textPanel, BorderLayout.CENTER);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        statsPanel.setBorder(new TitledBorder("Statistics"));
        statsPanel.setPreferredSize(new Dimension(200, 150));

        statsPanel.add(new JLabel("📊 Live Statistics:", SwingConstants.LEFT));
        statsPanel.add(wordCountLabel);
        statsPanel.add(charCountLabel);
        statsPanel.add(charNoSpaceLabel);
        statsPanel.add(sentenceCountLabel);
        statsPanel.add(paragraphCountLabel);

        // Preview panel
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(new TitledBorder("Preview (First 200 characters)"));
        JScrollPane previewScrollPane = new JScrollPane(previewPane);
        previewScrollPane.setPreferredSize(new Dimension(200, 120));
        previewPanel.add(previewScrollPane, BorderLayout.CENTER);

        // Right panel combining stats and preview
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(statsPanel, BorderLayout.NORTH);
        rightPanel.add(previewPanel, BorderLayout.CENTER);

        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loadSampleButton = createButton("Load Sample Text");
        JButton clearButton = createButton("Clear Text");

        loadSampleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText(SAMPLE_TEXT);
                textArea.setCaretPosition(0);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                updateStatistics();
            }
        });

        buttonPanel.add(loadSampleButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        button.setPreferredSize(new Dimension(140, 30));
        return button;
    }

    private void setupEventListeners() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateStatistics();
            }

            public void removeUpdate(DocumentEvent e) {
                updateStatistics();
            }

            public void changedUpdate(DocumentEvent e) {
                updateStatistics();
            }
        });
    }

    private void updateStatistics() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String text = textArea.getText();
                
                // Calculate statistics
                int wordCount = countWords(text);
                int charCount = text.length();
                int charNoSpaceCount = text.replaceAll("\\s", "").length();
                int sentenceCount = countSentences(text);
                int paragraphCount = countParagraphs(text);

                // Update labels
                wordCountLabel.setText("Words: " + wordCount);
                charCountLabel.setText("Characters: " + charCount);
                charNoSpaceLabel.setText("Characters (no spaces): " + charNoSpaceCount);
                sentenceCountLabel.setText("Sentences: " + sentenceCount);
                paragraphCountLabel.setText("Paragraphs: " + paragraphCount);

                // Update preview
                updatePreview(text);
            }
        });
    }

    private void updatePreview(String text) {
        if (text.length() <= 200) {
            previewPane.setText(text.isEmpty() ? "Preview will appear here..." : text);
        } else {
            previewPane.setText(text.substring(0, 200) + "...");
        }
        previewPane.setCaretPosition(0);
    }

    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        String[] words = text.trim().split("\\s+");
        return words.length;
    }

    private int countSentences(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        String[] sentences = text.split("[.!?]+");
        int count = 0;
        for (String sentence : sentences) {
            if (!sentence.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private int countParagraphs(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        String[] paragraphs = text.split("\\n\\s*\\n");
        int count = 0;
        for (String paragraph : paragraphs) {
            if (!paragraph.trim().isEmpty()) {
                count++;
            }
        }
        return Math.max(1, count); // At least 1 paragraph if there's text
    }

    private void setupWindow() {
        setTitle("Word Counter Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(null); // Center the window
        
        // Set minimum size
        setMinimumSize(new Dimension(700, 500));
        
        // Set look and feel - FIXED HERE
        try {
            // Use correct method to get system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            // Use default look and feel if system look and feel fails
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WordCounterApp().setVisible(true);
            }
        });
    }
}