//This file manages JSON persistence for tasks using a simple manual JSON implementation.
package todojavaapp.storage;

import todojavaapp.model.Priority;
import todojavaapp.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//This class provides methods to save and load tasks from a JSON file.
public class TaskStorage{
    private static final String DATA_FILE = "tasks.json";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    //This method saves a list of tasks to the JSON file.
    public void saveTasks(List<Task> tasks) throws IOException{
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < tasks.size(); i++){
            Task task = tasks.get(i);
            json.append("  {\n");
            json.append("    \"description\": \"").append(escapeJson(task.getDescription())).append("\",\n");
            json.append("    \"priority\": \"").append(task.getPriority().name()).append("\",\n");
            json.append("    \"dueDate\": ");
            if (task.getDueDate() != null){
                json.append("\"").append(task.getDueDate().format(DATE_FORMAT)).append("\"");
            } else{
                json.append("null");
            }
            json.append("\n");
            json.append("  }");
            if (i < tasks.size() - 1){
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("]\n");
        
        Files.write(Paths.get(DATA_FILE), json.toString().getBytes(StandardCharsets.UTF_8));
    }

    //This method loads tasks from the JSON file.
    public List<Task> loadTasks() throws IOException{
        List<Task> tasks = new ArrayList<>();
        
        if (!Files.exists(Paths.get(DATA_FILE))){
            return tasks;
        }
        
        String content = new String(Files.readAllBytes(Paths.get(DATA_FILE)), StandardCharsets.UTF_8);
        content = content.trim();
        
        if (content.isEmpty() || content.equals("[]")){
            return tasks;
        }
        
        content = content.substring(1, content.length() - 1).trim();
        
        List<String> objects = splitJsonObjects(content);
        
        for (String obj : objects){
            Task task = parseTask(obj);
            if (task != null){
                tasks.add(task);
            }
        }
        
        return tasks;
    }

    //This method splits the JSON array content into individual object strings.
    private List<String> splitJsonObjects(String content){
        List<String> objects = new ArrayList<>();
        int braceLevel = 0;
        int start = -1;
        
        for (int i = 0; i < content.length(); i++){
            char c = content.charAt(i);
            if (c == '{'){
                if (braceLevel == 0){
                    start = i;
                }
                braceLevel++;
            } else if (c == '}'){
                braceLevel--;
                if (braceLevel == 0 && start >= 0){
                    objects.add(content.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        
        return objects;
    }

    //This method parses a single task from a JSON object string.
    private Task parseTask(String json){
        try{
            String description = extractJsonValue(json, "description");
            String priorityStr = extractJsonValue(json, "priority");
            String dueDateStr = extractJsonValue(json, "dueDate");
            
            Priority priority = Priority.valueOf(priorityStr);
            LocalDate dueDate = null;
            if (dueDateStr != null && !dueDateStr.equals("null")){
                dueDate = LocalDate.parse(dueDateStr, DATE_FORMAT);
            }
            
            return new Task(description, priority, dueDate);
        } catch (Exception e){
            System.err.println("Failed to parse task: " + e.getMessage());
            return null;
        }
    }

    //This method extracts a value for a given key from a JSON object string.
    private String extractJsonValue(String json, String key){
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1){
            return null;
        }
        
        int colonIndex = json.indexOf(':', keyIndex);
        if (colonIndex == -1){
            return null;
        }
        
        int valueStart = colonIndex + 1;
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))){
            valueStart++;
        }
        
        if (valueStart >= json.length()){
            return null;
        }
        
        char firstChar = json.charAt(valueStart);
        if (firstChar == '"'){
            int valueEnd = json.indexOf('"', valueStart + 1);
            if (valueEnd == -1){
                return null;
            }
            return json.substring(valueStart + 1, valueEnd);
        } else if (firstChar == 'n' && json.startsWith("null", valueStart)){
            return "null";
        } else{
            int valueEnd = valueStart;
            while (valueEnd < json.length() && json.charAt(valueEnd) != ',' && json.charAt(valueEnd) != '}' && json.charAt(valueEnd) != '\n'){
                valueEnd++;
            }
            return json.substring(valueStart, valueEnd).trim();
        }
    }

    //This method escapes special characters in a string for JSON encoding.
    private String escapeJson(String str){
        if (str == null){
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
