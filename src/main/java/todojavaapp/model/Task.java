//This file defines the task model with description priority and due date.
package todojavaapp.model;

import java.time.LocalDate;
import java.util.Objects;

//This class holds the data for a single task row.
public class Task{
    private String description;
    private Priority priority;
    private LocalDate dueDate;

    //This constructor initializes all fields for this task.
    public Task(String description, Priority priority, LocalDate dueDate){
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    //This method returns the description.
    public String getDescription(){
        return description;
    }

    //This method updates the description.
    public void setDescription(String description){
        this.description = description;
    }

    //This method returns the priority.
    public Priority getPriority(){
        return priority;
    }

    //This method updates the priority.
    public void setPriority(Priority priority){
        this.priority = priority;
    }

    //This method returns the due date.
    public LocalDate getDueDate(){
        return dueDate;
    }

    //This method updates the due date.
    public void setDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }

    //This method defines equality based on all fields.
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(description, task.description) && priority == task.priority && Objects.equals(dueDate, task.dueDate);
    }

    //This method returns a hash code based on all fields.
    @Override
    public int hashCode(){
        return Objects.hash(description, priority, dueDate);
    }
}
