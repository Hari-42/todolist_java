import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * A simple To-Do List application with a GUI for managing tasks.
 */
public class ToDoListApp {

    private JFrame frame;
    private JTextField taskInputField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private ArrayList<String> tasks;

    /**
     * Constructor to initialize the application.
     */
    public ToDoListApp() {
        tasks = new ArrayList<>();
        taskListModel = new DefaultListModel<>();

        // Load tasks from the file if they exist
        loadTasksFromFile();

        // Set up the GUI components
        setupFrame();
    }

    /**
     * Initializes and sets up the main frame and its components.
     */
    private void setupFrame() {
        frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Input panel for adding tasks
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel taskLabel = new JLabel("Task:");
        taskInputField = new JTextField(20);
        JButton addButton = new JButton("Add");

        inputPanel.add(taskLabel);
        inputPanel.add(taskInputField);
        inputPanel.add(addButton);

        // Scrollable list to display tasks
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);

        // Panel for removing tasks
        JPanel actionPanel = new JPanel();
        JButton removeButton = new JButton("Remove Selected");
        actionPanel.add(removeButton);

        // Add panels to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(actionPanel, BorderLayout.SOUTH);

        // Set up button actions
        addButton.addActionListener(new AddTaskAction());
        removeButton.addActionListener(new RemoveTaskAction());

        frame.setVisible(true);
    }

    /**
     * Loads tasks from a file into the task list.
     */
    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String task;
            while ((task = reader.readLine()) != null) {
                tasks.add(task);
                taskListModel.addElement(task);
            }
        } catch (IOException e) {
            // If the file doesn't exist or there's an error, start with an empty list
            System.err.println("No existing task file found. Starting fresh.");
        }
    }

    /**
     * Saves tasks from the list to the file.
     */
    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to save tasks to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ActionListener for adding tasks.
     */
    private class AddTaskAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String task = taskInputField.getText().trim();
            if (!task.isEmpty()) {
                tasks.add(task);
                taskListModel.addElement(task);
                taskInputField.setText("");
                saveTasksToFile();
            } else {
                JOptionPane.showMessageDialog(frame, "Task cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * ActionListener for removing selected tasks.
     */
    private class RemoveTaskAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String removedTask = taskListModel.remove(selectedIndex);
                tasks.remove(removedTask);
                saveTasksToFile();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a task to remove.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Main method to run the To-Do List application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
