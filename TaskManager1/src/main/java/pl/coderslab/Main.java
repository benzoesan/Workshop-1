package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static pl.coderslab.ConsoleColors.*;

public class Main {
    static String [][] tasks = new String[0][];
    static final String FILE_NAME = "tasks.csv";
    private static final String EXIT_OPTION = "exit";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};

    public static void main(String[] args) {
        getTasks();
        while (true) {
            showOptions(OPTIONS);
            String option = selectOption();
            if (!validOption(option)){
                errorMsg(option);
                continue;
            }
            switchOption(option);
            if(isExit(option)){
                break;
            }
        }
        getDate();
        exitMsg();

    }
    public static void showOptions(String []option ){

        System.out.println(BLUE+"Please select an option:"+RESET);

        for (int i = 0; i <OPTIONS.length; i++) {
            System.out.println(OPTIONS[i]);
        }
    }

    public static void getDate(){
        try (PrintWriter writer = new PrintWriter(new File(FILE_NAME))) {
            for (String[] task : tasks) {
                String taskLine = StringUtils.join(task, ",");
                writer.println(taskLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static void getTasks(){
        StringBuilder dataBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            while (scanner.hasNextLine()) {
                dataBuilder.append(scanner.nextLine()).append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] taskLines = dataBuilder.toString().split(";");
        for (String taskLine : taskLines) {
            String[] task = taskLine.split(",");
            tasks = ArrayUtils.add(tasks, task);
        }
    }
    public static void exitMsg(){
        System.out.println(RED + "Bye, bye" + RESET);
    }
    public static void switchOption (String option){
        switch (option) {
            case "add": {
                addTask();
                break;
            }
            case "remove": {
                removeTask();
                break;
            }
            case "list": {
                listTasks();
                break;
            }
            default: {
                break;
            }
        }

    }
    public static void listTasks() {
        for (int i = 0; i < tasks.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.print(tasks[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void removeTask() {
        if (tasks.length == 0) {
            System.out.println(RED + "No tasks to remove" + RESET);
            return;
        }
        System.out.print(BLUE + "Please select index to remove > " + RESET);
        Scanner scanner = new Scanner(System.in);
        int index;
        while (true) {
            while (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.print(RED + "Invalid argument passed. Please give number between 0 and " + (tasks.length - 1) + ": " + RESET);
            }
            index = scanner.nextInt();
            if (index >= 0 && index < tasks.length) {
                break;
            } else {
                System.out.print(RED + "Invalid argument passed. Please give number between 0 and " + (tasks.length - 1) + ": " + RESET);
            }
        }
        String[] task = tasks[index];
        System.out.print(BLUE + "Please confirm (Y/y) to remove task '" + task[0] + "' > " + RESET);
        scanner = new Scanner(System.in);
        String confirmed = scanner.nextLine();
        if ("y".equalsIgnoreCase(confirmed)) {
            tasks = ArrayUtils.remove(tasks, index);
            System.out.println("Task was successfully deleted");
        }
    }

    public static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please add task description > ");
        String description = scanner.nextLine().trim();
        System.out.print("Please add task due date > ");
        String dueDate = scanner.nextLine().trim();
        String important = null;

        do {
            System.out.print("Is your task important (true/false)? > ");
            important = scanner.nextLine().trim();
        } while (!("false".equals(important) || "true".equals(important)));
        tasks = ArrayUtils.add(tasks, new String[]{description, dueDate, important});
        System.out.println("Task was successfully added");
        }
    public static boolean isExit(String option){
        return EXIT_OPTION.equals(option);

    }
    private static boolean validOption(String option) {
        return ArrayUtils.contains(OPTIONS, option);
    }
    private static void errorMsg (String option) {
        System.out.println(RED + "Invalid menu option: '" + option + "'" + RESET);
    }

    public static String selectOption() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim().toLowerCase();
    }
}