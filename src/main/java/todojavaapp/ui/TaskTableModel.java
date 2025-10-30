//This file defines a table model for tasks used by the tasks table.
package todojavaapp.ui;

import todojavaapp.model.Priority;
import todojavaapp.model.Task;
import todojavaapp.storage.TaskStorage;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//This class provides a table model with support for filtering and basic operations.
public class TaskTableModel extends AbstractTableModel{
    private final List<Task> allTasks = new ArrayList<>();
    private final List<Task> viewTasks = new ArrayList<>();
    private final TaskStorage storage = new TaskStorage();

    private Priority filterPriority = null;
    private DateFilter filterDate = DateFilter.ALL;

    private static final String[] COLS ={"Description", "Priority", "Due Date"};
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //This enum defines the time based filter options.
    public enum DateFilter{
        ALL("All"),
        TODAY("Today"),
        THIS_WEEK("This week"),
        OVERDUE("Overdue");
        private final String label;
        DateFilter(String label){ this.label = label; }
        public String getLabel(){ return label; }
        @Override public String toString(){ return label; }
    }

    //This constructor loads tasks from JSON storage.
    public TaskTableModel(){
        loadFromStorage();
    }

    //This method loads tasks from the JSON file.
    private void loadFromStorage(){
        try{
            List<Task> loaded = storage.loadTasks();
            allTasks.clear();
            allTasks.addAll(loaded);
            applyFilters();
        } catch (IOException e){
            System.err.println("Failed to load tasks: " + e.getMessage());
        }
    }

    //This method saves tasks to the JSON file.
    private void saveToStorage(){
        try{
            storage.saveTasks(allTasks);
        } catch (IOException e){
            System.err.println("Failed to save tasks: " + e.getMessage());
        }
    }

    //This method returns the row count in the current view.
    @Override
    public int getRowCount(){
        return viewTasks.size();
    }

    //This method returns the number of columns in the table.
    @Override
    public int getColumnCount(){
        return COLS.length;
    }

    //This method returns the column name.
    @Override
    public String getColumnName(int column){
        return COLS[column];
    }

    //This method returns the value to display for a cell.
    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        Task t = viewTasks.get(rowIndex);
        switch (columnIndex){
            case 0: return t.getDescription();
            case 1: return t.getPriority().getDisplay();
            case 2: return t.getDueDate() != null ? DTF.format(t.getDueDate()) : "";
            default: return "";
        }
    }

    //This method adds a new task and reapplies filters.
    public void addTask(Task t){
        allTasks.add(Objects.requireNonNull(t));
        saveToStorage();
        applyFilters();
    }

    //This method updates a task at an index and reapplies filters.
    public void updateTask(int viewRow, Task updated){
        Task existing = viewTasks.get(viewRow);
        int realIndex = allTasks.indexOf(existing);
        if (realIndex >= 0){
            allTasks.set(realIndex, Objects.requireNonNull(updated));
            saveToStorage();
            applyFilters();
        }
    }

    //This method removes a task at an index and reapplies filters.
    public void removeTask(int viewRow){
        Task existing = viewTasks.get(viewRow);
        allTasks.remove(existing);
        saveToStorage();
        applyFilters();
    }

    //This method returns the task at an index in the view.
    public Task getTaskAt(int viewRow){
        return viewTasks.get(viewRow);
    }

    //This method sets the priority filter and reapplies filters.
    public void setPriorityFilter(Priority p){
        this.filterPriority = p;
        applyFilters();
    }

    //This method sets the date filter and reapplies filters.
    public void setDateFilter(DateFilter d){
        this.filterDate = d == null ? DateFilter.ALL : d;
        applyFilters();
    }

    //This method filters all tasks into the view list based on current filters.
    private void applyFilters(){
        List<Task> filtered = allTasks.stream().filter(t ->{
            boolean okPriority = filterPriority == null || t.getPriority() == filterPriority;
            boolean okDate = matchesDateFilter(t.getDueDate(), filterDate);
            return okPriority && okDate;
        }).collect(Collectors.toList());
        viewTasks.clear();
        viewTasks.addAll(filtered);
        fireTableDataChanged();
    }

    //This method returns whether a date matches the date filter.
    private boolean matchesDateFilter(LocalDate d, DateFilter f){
        if (f == DateFilter.ALL) return true;
        if (d == null) return false;
        LocalDate today = LocalDate.now();
        switch (f){
            case TODAY:
                return d.isEqual(today);
            case THIS_WEEK:
                LocalDate start = today.minusDays(today.getDayOfWeek().getValue() - 1);
                LocalDate end = start.plusDays(6);
                return (d.isEqual(start) || d.isAfter(start)) && (d.isEqual(end) || d.isBefore(end));
            case OVERDUE:
                return d.isBefore(today);
            default:
                return true;
        }
    }
}
