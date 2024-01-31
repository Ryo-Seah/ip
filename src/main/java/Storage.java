import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
        createFolderAndFile(filePath);

    }

    private static void createFolderAndFile(String filePath) {
        try {
            File dataFolder = new File("./data/");
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            File dataFile = new File(filePath);
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating data folder and file: " + e.getMessage());
        }
    }

    public  ArrayList<Task> loadTasks() {
        ArrayList<Task> loadedTasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] taskData = line.split(" \\| ");
                if (taskData.length >= 4) {
                    TaskType type = TaskType.valueOf(taskData[0]);
                    String name = taskData[1];
                    Boolean done = Integer.parseInt(taskData[2]) == 1;
                    String startTime = taskData.length > 3 ? taskData[3] : "";
                    String endTime = taskData.length > 4 ? taskData[4] : "";

                    switch(type) {

                        case T:
                            loadedTasks.add(new Task(name, type, done));
                            break;
                        case D:
                            loadedTasks.add(new Task(name, type, done, startTime));
                            break;

                        case E:
                            loadedTasks.add(new Task(name, type, done, startTime, endTime));
                            break;

                    }

                } else {
                    System.out.println("Warning: Ignored corrupted line in the file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }

        return loadedTasks;
    }

    public void saveTasks(TaskList taskList) {
        try (FileWriter fw = new FileWriter(this.filePath)) {
            for (Task task : taskList.getTasks()) {
                fw.write(task.getTaskType().name() + " | " +  task.getName()  + " | " + (task.isDone() ? 1 : 0) + " | " + task.getStartDateString() + " | " + task.getEndDateString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }
}