//This file defines a modal dialog for adding or editing a task.
package todojavaapp.ui;

import todojavaapp.model.Priority;
import todojavaapp.model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//This class shows a simple dialog that gathers description priority and due date.
public class TaskDialog extends JDialog{
    private final JTextField descriptionField = new JTextField();
    private final JComboBox<Priority> priorityCombo = new JComboBox<>(Priority.values());
    private final JTextField dateField = new JTextField();

    private Task result;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //This constructor builds the dialog with inputs and buttons.
    public TaskDialog(Window owner, String title, Task initial){
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        descriptionField.setFont(inputFont);
        descriptionField.setPreferredSize(new Dimension(300, 32));
        priorityCombo.setFont(inputFont);
        priorityCombo.setPreferredSize(new Dimension(300, 32));
        dateField.setFont(inputFont);
        dateField.setPreferredSize(new Dimension(300, 32));

        gbc.weightx = 0;
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(labelFont);
        form.add(descLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(descriptionField, gbc);

        gbc.weightx = 0; gbc.gridx = 0; gbc.gridy = 1;
        JLabel prioLabel = new JLabel("Priority:");
        prioLabel.setFont(labelFont);
        form.add(prioLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(priorityCombo, gbc);

        gbc.weightx = 0; gbc.gridx = 0; gbc.gridy = 2;
        JLabel dateLabel = new JLabel("Due Date:");
        dateLabel.setFont(labelFont);
        form.add(dateLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(dateField, gbc);

        JLabel hint = new JLabel("(Use format: yyyy-MM-dd, like 2025-10-29)");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(new Color(100, 100, 120));

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(new Color(245, 245, 250));
        south.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel hintPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hintPanel.setBackground(new Color(245, 245, 250));
        hintPanel.add(hint);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton ok = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        ok.setFont(btnFont);
        cancel.setFont(btnFont);
        ok.setPreferredSize(new Dimension(100, 35));
        cancel.setPreferredSize(new Dimension(100, 35));

        ok.setBackground(new Color(46, 204, 113));
        ok.setForeground(Color.WHITE);
        ok.setOpaque(true);
        ok.setBorderPainted(false);
        ok.setFocusPainted(false);
        ok.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cancel.setBackground(new Color(149, 165, 166));
        cancel.setForeground(Color.WHITE);
        cancel.setOpaque(true);
        cancel.setBorderPainted(false);
        cancel.setFocusPainted(false);
        cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(ok);
        buttonPanel.add(cancel);

        south.add(hintPanel, BorderLayout.WEST);
        south.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(form, BorderLayout.CENTER);
        mainPanel.add(south, BorderLayout.SOUTH);

        add(mainPanel);

        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e ->{ result = null; dispose(); });

        if (initial != null){
            descriptionField.setText(initial.getDescription());
            priorityCombo.setSelectedItem(initial.getPriority());
            dateField.setText(initial.getDueDate() != null ? DTF.format(initial.getDueDate()) : "");
        }

        pack();
        setLocationRelativeTo(owner);
        setMinimumSize(new Dimension(500, getHeight()));
    }

    //This method validates inputs and constructs the result task.
    private void onOk(){
        String desc = descriptionField.getText().trim();
        if (desc.isEmpty()){
            JOptionPane.showMessageDialog(this, 
                "Task description is required.\nPlease enter a description.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            descriptionField.requestFocus();
            return;
        }
        Priority pr = (Priority) priorityCombo.getSelectedItem();
        LocalDate date = null;
        String text = dateField.getText().trim();
        if (!text.isEmpty()){
            try{
                date = LocalDate.parse(text, DTF);
            } catch (DateTimeParseException ex){
                JOptionPane.showMessageDialog(this, 
                    "Invalid date format.\nPlease use: yyyy-MM-dd (like 2025-10-29)", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                dateField.requestFocus();
                return;
            }
        }
        result = new Task(desc, pr == null ? Priority.MEDIUM : pr, date);
        dispose();
    }

    //This method returns the created or edited task or null if canceled.
    public Task getResult(){
        return result;
    }
}
