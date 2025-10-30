//This file starts the application on the event dispatch thread and shows the main window.
package todojavaapp;

import todojavaapp.ui.ToDoFrame;

import javax.swing.*;

//This class contains the main method that launches the to do application.
public class App{
    //This method launches the main frame on the event dispatch thread.
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignore){
            }
            new ToDoFrame().setVisible(true);
        });
    }
}
