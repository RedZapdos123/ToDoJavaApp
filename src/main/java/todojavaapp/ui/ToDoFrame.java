//This file defines the main window of the to do application.
package todojavaapp.ui;

import todojavaapp.model.Priority;
import todojavaapp.model.Task;
import todojavaapp.ui.TaskTableModel.DateFilter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

//This class builds the main frame containing table filters and colored action buttons.
public class ToDoFrame extends JFrame{
    private final TaskTableModel model = new TaskTableModel();
    private final JTable table = new JTable(model);

    private final JComboBox<String> priorityFilter = new JComboBox<>(new String[]{"All", "High", "Medium", "Low"});
    private final JComboBox<DateFilter> dateFilter = new JComboBox<>(DateFilter.values());

    private final JButton addBtn = new JButton("Add Task");
    private final JButton editBtn = new JButton("Edit Task");
    private final JButton deleteBtn = new JButton("Delete Task");

    //This constructor lays out the components and wires event handlers.
    public ToDoFrame(){
        super("ToDo Java App: Made by Mridankan Mandal");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(220, 220, 230));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionBackground(new Color(100, 149, 237));
        table.setSelectionForeground(Color.WHITE);

        alignColumns();

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));
        header.setReorderingAllowed(false);
        header.setOpaque(true);
        header.setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setFont(new Font("Segoe UI", Font.BOLD, 15));
                label.setBackground(new Color(70, 130, 180));
                label.setForeground(Color.WHITE);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        filters.setBackground(new Color(245, 245, 250));
        JLabel priorityLabel = new JLabel("Filter by Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filters.add(priorityLabel);
        priorityFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        priorityFilter.setPreferredSize(new Dimension(120, 30));
        filters.add(priorityFilter);
        filters.add(Box.createHorizontalStrut(20));
        JLabel dateLabel = new JLabel("Filter by Due Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filters.add(dateLabel);
        dateFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateFilter.setPreferredSize(new Dimension(140, 30));
        filters.add(dateFilter);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        actions.setBackground(new Color(245, 245, 250));
        styleButtons();
        actions.add(addBtn);
        actions.add(editBtn);
        actions.add(deleteBtn);

        mainPanel.add(filters, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(actions, BorderLayout.SOUTH);

        add(mainPanel);

        hookEvents();
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    //This method configures colors and rendering for buttons and table.
    private void styleButtons(){
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        Dimension btnSize = new Dimension(130, 38);

        paintButton(addBtn, new Color(46, 204, 113), Color.WHITE, btnFont, btnSize);
        paintButton(editBtn, new Color(52, 152, 219), Color.WHITE, btnFont, btnSize);
        paintButton(deleteBtn, new Color(231, 76, 60), Color.WHITE, btnFont, btnSize);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }

    //This method sets background and text color with cross Look and Feel hints.
    private void paintButton(JButton b, Color bg, Color fg, Font font, Dimension size){
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(font);
        b.setPreferredSize(size);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    //This method aligns specific columns for better readability.
    private void alignColumns(){
        table.getColumnModel().getColumn(0).setPreferredWidth(450);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
    }

    //This method attaches listeners for filters selection and button clicks.
    private void hookEvents(){
        priorityFilter.addActionListener(e -> onPriorityFilter());
        dateFilter.addActionListener(e -> onDateFilter());
        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        deleteBtn.addActionListener(e -> onDelete());

        table.getSelectionModel().addListSelectionListener(this::onSelectionChanged);
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
    }

    //This method updates enabled state based on selection.
    private void onSelectionChanged(ListSelectionEvent e){
        boolean hasSel = table.getSelectedRow() >= 0;
        editBtn.setEnabled(hasSel);
        deleteBtn.setEnabled(hasSel);
    }

    //This method applies priority filter to the model.
    private void onPriorityFilter(){
        int idx = priorityFilter.getSelectedIndex();
        switch (idx){
            case 1: model.setPriorityFilter(Priority.HIGH); break;
            case 2: model.setPriorityFilter(Priority.MEDIUM); break;
            case 3: model.setPriorityFilter(Priority.LOW); break;
            default: model.setPriorityFilter(null); break;
        }
    }

    //This method applies date filter to the model.
    private void onDateFilter(){
        DateFilter d = (DateFilter) dateFilter.getSelectedItem();
        model.setDateFilter(d);
    }

    //This method opens a dialog to add a task and saves it when valid.
    private void onAdd(){
        TaskDialog dlg = new TaskDialog(this, "Add Task", null);
        dlg.setVisible(true);
        Task t = dlg.getResult();
        if (t != null){
            model.addTask(t);
        }
    }

    //This method opens a dialog to edit the selected task and saves it when valid.
    private void onEdit(){
        int row = table.getSelectedRow();
        if (row < 0) return;
        Task current = model.getTaskAt(row);
        TaskDialog dlg = new TaskDialog(this, "Edit Task", current);
        dlg.setVisible(true);
        Task updated = dlg.getResult();
        if (updated != null){
            model.updateTask(row, updated);
        }
    }

    //This method deletes the selected task after confirmation.
    private void onDelete(){
        int row = table.getSelectedRow();
        if (row < 0) return;
        Task task = model.getTaskAt(row);
        String msg = String.format("Delete task:\n\"%s\"?", task.getDescription());
        int ans = JOptionPane.showConfirmDialog(this, msg, "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ans == JOptionPane.YES_OPTION){
            model.removeTask(row);
        }
    }
}
