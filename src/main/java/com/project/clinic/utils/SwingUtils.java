package com.project.clinic.utils;

import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class SwingUtils {
    private static JFrame loadingFrame;
    private static final String ICON_PATH = "src/main/resources/icon.png";
    private static final Font DEFAULT_FONT_BOLD_14 = new Font("Arial", Font.BOLD, 14);
    private static final Font DEFAULT_FONT_BOLD_16 = new Font("Arial", Font.BOLD, 16);
    private static final Color SHUTDOWN_BUTTON_COLOR = new Color(220, 53, 69); 
    private static final Color OPEN_SYSTEM_BUTTON_COLOR = new Color(34, 139, 34); 

    public static void showLoadingMessage() {
        loadingFrame = createFrame(300, 100, JFrame.DO_NOTHING_ON_CLOSE);
        JLabel loadingLabel = new JLabel("Loading system, please wait...", SwingConstants.CENTER);
        loadingFrame.add(loadingLabel, BorderLayout.CENTER);
        loadingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loadingFrame.setVisible(true);
    }

    
    public static void closeLoadingMessage() {
        if (loadingFrame != null) {
            loadingFrame.dispose();
        }
    }

  
    public static void openUIWindow(ApplicationContext context) {
        String url = NetworkUtil.getUrl(context);
        System.setProperty("java.awt.headless", "false"); 

        JFrame frame = createFrame(600, 400, JFrame.EXIT_ON_CLOSE);
        JPanel panel = createMainPanel(url, context);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JFrame createFrame(int width, int height, int closeOperation) {
        JFrame frame = new JFrame("Clinic System");
        frame.setIconImage(new ImageIcon(ICON_PATH).getImage());
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(closeOperation);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private static JPanel createMainPanel(String url, ApplicationContext context) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(createWarningLabel());
        panel.add(Box.createVerticalStrut(20)); 
        panel.add(createUrlPanel(url));
        panel.add(Box.createVerticalStrut(20)); 
        panel.add(createOpenSystemButton(context));
        panel.add(Box.createVerticalStrut(20)); 
        panel.add(createShutdownButton());

        return panel;
    }

    private static JLabel createWarningLabel() {
        JLabel warningLabel = new JLabel("<html>If you close this window, the system will shut down.<br>The system will remain running until this window is closed or you click the 'Shutdown System' button.</html>");
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        warningLabel.setFont(DEFAULT_FONT_BOLD_14);
        warningLabel.setForeground(Color.RED);
        return warningLabel;
    }

    private static JPanel createUrlPanel(String url) {
        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.X_AXIS));
        urlPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField urlField = createUrlField(url);
        JButton copyButton = createCopyButton(url);

        urlPanel.add(urlField);
        urlPanel.add(Box.createRigidArea(new Dimension(10, 0))); 
        urlPanel.add(copyButton);

        return urlPanel;
    }

    private static JTextField createUrlField(String url) {
        JTextField urlField = new JTextField("System running at: " + url);
        urlField.setFont(DEFAULT_FONT_BOLD_14);
        urlField.setEditable(false);
        urlField.setBackground(null);
        urlField.setBorder(null);
        urlField.setHorizontalAlignment(JTextField.CENTER);
        return urlField;
    }

    private static JButton createCopyButton(String url) {
        JButton copyButton = new JButton("Copy");
        copyButton.setFont(new Font("Arial", Font.BOLD, 12));
        copyButton.setPreferredSize(new Dimension(80, 30));
        copyButton.addActionListener(e -> copyToClipboard(url));
        return copyButton;
    }

    private static void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        JOptionPane.showMessageDialog(null, "URL copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
    }

    private static JButton createShutdownButton() {
        JButton shutdownButton = new JButton("Shutdown System");
        shutdownButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shutdownButton.setPreferredSize(new Dimension(100, 45));
        shutdownButton.setFont(DEFAULT_FONT_BOLD_16);
        shutdownButton.setBackground(SHUTDOWN_BUTTON_COLOR);
        shutdownButton.setForeground(Color.WHITE);
        shutdownButton.addActionListener(e -> System.exit(0));
        return shutdownButton;
    }

    private static JButton createOpenSystemButton(ApplicationContext context) {
        JButton openSystemButton = new JButton("Open in Browser");
        openSystemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openSystemButton.setPreferredSize(new Dimension(220, 45));
        openSystemButton.setFont(DEFAULT_FONT_BOLD_16);
        openSystemButton.setBackground(OPEN_SYSTEM_BUTTON_COLOR);
        openSystemButton.setForeground(Color.WHITE);

        openSystemButton.addActionListener(e -> NetworkUtil.openBrowser(NetworkUtil.getUrl(context)));
        return openSystemButton;
    }
}
