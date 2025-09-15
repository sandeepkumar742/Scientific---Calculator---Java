import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class ScientificCalculator extends JFrame implements ActionListener {
    private JTextField textField;
    private JTextArea historyArea;
    private String operator = "";
    private double num1 = 0, num2 = 0, result = 0;
    private boolean isOperatorPressed = false;
    private double memory = 0; // Memory storage
    private final DecimalFormat df = new DecimalFormat("0.######");
    private final LinkedList<String> history = new LinkedList<>();

    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(650, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Display field =====
        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setEditable(false);
        add(textField, BorderLayout.NORTH);

        // ===== Buttons Panel =====
        JPanel panel = new JPanel(new GridLayout(7, 5, 8, 8));

        String[] buttons = {
            "7", "8", "9", "/", "MC",
            "4", "5", "6", "*", "MR",
            "1", "2", "3", "-", "M+",
            "0", ".", "=", "+", "M-",
            "√", "x²", "%", "1/x", "±",
            "sin", "cos", "tan", "log", "ln",
            "exp", "C"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.addActionListener(this);
            panel.add(btn);
        }
        add(panel, BorderLayout.CENTER);

        // ===== History Panel =====
        JPanel historyPanel = new JPanel(new BorderLayout(5, 5));

        historyArea = new JTextArea(10, 15);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(historyArea);
        historyPanel.add(scroll, BorderLayout.CENTER);

        // Clear History Button
        JButton clearHistoryBtn = new JButton("Clear History");
        clearHistoryBtn.addActionListener(_ -> {
            history.clear();
            historyArea.setText("");
        });
        historyPanel.add(clearHistoryBtn, BorderLayout.SOUTH);

        add(historyPanel, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        try {
            if (cmd.matches("[0-9]") || cmd.equals(".")) {
                if (isOperatorPressed) {
                    textField.setText("");
                    isOperatorPressed = false;
                }
                textField.setText(textField.getText() + cmd);
            }
            else if (cmd.matches("[+\\-*/]")) {
                num1 = Double.parseDouble(textField.getText());
                operator = cmd;
                isOperatorPressed = true;
            }
            else if (cmd.equals("=")) {
                num2 = Double.parseDouble(textField.getText());
                switch (operator) {
                    case "+": result = num1 + num2; break;
                    case "-": result = num1 - num2; break;
                    case "*": result = num1 * num2; break;
                    case "/": result = (num2 != 0) ? num1 / num2 : Double.NaN; break;
                }
                textField.setText(df.format(result));
                addToHistory(num1 + " " + operator + " " + num2 + " = " + df.format(result));
            }
            else if (cmd.equals("C")) {
                textField.setText("");
                num1 = num2 = result = 0;
                operator = "";
            }
            else if (cmd.equals("√")) {
                num1 = Double.parseDouble(textField.getText());
                result = (num1 >= 0) ? Math.sqrt(num1) : Double.NaN;
                textField.setText(df.format(result));
                addToHistory("√(" + num1 + ") = " + df.format(result));
            }
            else if (cmd.equals("x²")) {
                num1 = Double.parseDouble(textField.getText());
                result = num1 * num1;
                textField.setText(df.format(result));
                addToHistory(num1 + "² = " + df.format(result));
            }
            else if (cmd.equals("%")) {
                num1 = Double.parseDouble(textField.getText());
                result = num1 / 100;
                textField.setText(df.format(result));
                addToHistory(num1 + "% = " + df.format(result));
            }
            else if (cmd.equals("1/x")) {
                num1 = Double.parseDouble(textField.getText());
                result = (num1 != 0) ? 1 / num1 : Double.NaN;
                textField.setText(df.format(result));
                addToHistory("1/" + num1 + " = " + df.format(result));
            }
            else if (cmd.equals("±")) {
                num1 = Double.parseDouble(textField.getText());
                result = -num1;
                textField.setText(df.format(result));
            }
            else if (cmd.equals("sin")) {
                num1 = Double.parseDouble(textField.getText());
                result = Math.sin(Math.toRadians(num1));
                textField.setText(df.format(result));
                addToHistory("sin(" + num1 + ") = " + df.format(result));
            }
            else if (cmd.equals("cos")) {
                num1 = Double.parseDouble(textField.getText());
                result = Math.cos(Math.toRadians(num1));
                textField.setText(df.format(result));
                addToHistory("cos(" + num1 + ") = " + df.format(result));
            }
            else if (cmd.equals("tan")) {
                num1 = Double.parseDouble(textField.getText());
                result = Math.tan(Math.toRadians(num1));
                textField.setText(df.format(result));
                addToHistory("tan(" + num1 + ") = " + df.format(result));
            }
            else if (cmd.equals("log")) {
                num1 = Double.parseDouble(textField.getText());
                result = (num1 > 0) ? Math.log10(num1) : Double.NaN;
                textField.setText(df.format(result));
                addToHistory("log(" + num1 + ") = " + df.format(result));
            }
            else if (cmd.equals("ln")) {
                num1 = Double.parseDouble(textField.getText());
                result = (num1 > 0) ? Math.log(num1) : Double.NaN;
                textField.setText(df.format(result));
                addToHistory("ln(" + num1 + ") = " + df.format(result));
            }
            else if (cmd.equals("exp")) {
                num1 = Double.parseDouble(textField.getText());
                result = Math.exp(num1);
                textField.setText(df.format(result));
                addToHistory("exp(" + num1 + ") = " + df.format(result));
            }
            // Memory Functions
            else if (cmd.equals("MC")) {
                memory = 0;
                historyArea.append("Memory cleared\n");
            }
            else if (cmd.equals("MR")) {
                textField.setText(df.format(memory));
                historyArea.append("Memory recalled: " + df.format(memory) + "\n");
            }
            else if (cmd.equals("M+")) {
                memory += Double.parseDouble(textField.getText());
                historyArea.append("Memory added: " + df.format(memory) + "\n");
            }
            else if (cmd.equals("M-")) {
                memory -= Double.parseDouble(textField.getText());
                historyArea.append("Memory subtracted: " + df.format(memory) + "\n");
            }

        } catch (Exception ex) {
            textField.setText("Error");
        }
    }

    private void addToHistory(String record) {
        history.addFirst(record);
        if (history.size() > 10) history.removeLast();
        historyArea.setText("");
        for (String h : history) {
            historyArea.append(h + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScientificCalculator().setVisible(true));
}
}