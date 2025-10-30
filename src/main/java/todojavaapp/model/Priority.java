//This file defines the priority levels for tasks.
package todojavaapp.model;

//This enum represents three priority levels that the user can select.
public enum Priority{
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private final String display;

    //This constructor stores a readable display label.
    Priority(String display){
        this.display = display;
    }

    //This method returns the readable display label.
    public String getDisplay(){
        return display;
    }

    //This method returns the enum from a display label.
    public static Priority fromDisplay(String d){
        for (Priority p : values()){
            if (p.display.equalsIgnoreCase(d)){
                return p;
            }
        }
        return null;
    }

    //This method returns the enum as its display string.
    @Override
    public String toString(){
        return display;
    }
}
