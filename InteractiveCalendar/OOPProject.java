
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
import java.util.Scanner;
import javax.swing.*;

public class OOPProject extends JFrame {

    // Declaration of GUI components
    private JLabel monthLabel;
    private JPanel calendarPanel;
    private JButton[] dayButtons; // Array to store each day
    private Calendar currentCalendar;
    private String[] tasks; // Array to store tasks for each day

    JRadioButtonMenuItem rb1 = new JRadioButtonMenuItem("Bold");
    JRadioButtonMenuItem rb2 = new JRadioButtonMenuItem("Italic");
    JMenuItem fmItem1 = new JMenuItem("Dialog");
    JMenuItem fmItem2 = new JMenuItem("DialogInput");
    JMenuItem fmItem3 = new JMenuItem("Monospaced");
    JMenuItem fmItem4 = new JMenuItem("SansSerif");
    JMenuItem fmItem5 = new JMenuItem("Serif");
    JMenuBar taskManagerMenuBar = new JMenuBar();
    JTextField taskTF = new JTextField();
    JTextField dayTF = new JTextField(5);
    JTextField monthTF = new JTextField(5);
    JTextField yearTF = new JTextField(5);
    JLabel dateLabel = new JLabel("Enter date(DAY/MONTH/YEAR): ");
    JLabel taskLabel = new JLabel("Enter task:");
    JTextArea taskTextArea = new JTextArea();

    // Constructor
    public OOPProject() {

        // Setting up JFrame
        super.setTitle("My Calendar");
        super.setSize(750, 500);
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialization and setup of various GUI components
        monthLabel = new JLabel("", JLabel.CENTER);
        calendarPanel = new JPanel(new GridLayout(0, 7)); // Grid layout for calendar display
        dayButtons = new JButton[42]; // 6 rows * 7 columns for days
        currentCalendar = Calendar.getInstance(); // Getting the current system calendar
        updateCalendar(); // Update calendar display

        // Initializing navigation buttons and adding actionListener
        JButton prevButton = new JButton("<< Prev");   // Allows you to go back one month
        prevButton.addActionListener(new prevMonth());
        JButton nextButton = new JButton("Next >>");    // Allows you to go forward one month
        nextButton.addActionListener(new nextMonth());

        // Buttons to save/load calendar data
        JButton saveButton = new JButton("Save");   // Allows you to save calendar
        saveButton.addActionListener(new saveAction());
        JButton loadButton = new JButton("Load");   // Allows you to load a saved calendar
        loadButton.addActionListener(new loadAction());

        // Button to add tasks for a particular day
        JButton addTaskButton = new JButton("Add Task");    // Allows you to add a task
        addTaskButton.addActionListener(new addTaskAction());

        // Setting up panels for different sections of the interface
        JPanel navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.add(prevButton, BorderLayout.WEST);
        navigationPanel.add(monthLabel, BorderLayout.CENTER);
        navigationPanel.add(nextButton, BorderLayout.EAST);
        Color navColor = new Color(204, 251, 255);
        navigationPanel.setBackground(navColor);
        JPanel fileOperationPanel = new JPanel();
        fileOperationPanel.add(saveButton);
        fileOperationPanel.add(loadButton);

        JPanel taskPanel = new JPanel();
        taskPanel.add(addTaskButton);
        taskPanel.setBackground(navColor);
        fileOperationPanel.setBackground(navColor);

        // Initializing menu bar
        JMenuBar menuBar = new JMenuBar();

        // Menus in the menu bar
        JMenu taskManagerMenu = new JMenu("Task Manager");
        JMenu fontMenu = new JMenu("Font");
        JMenu backgroundColorMenu = new JMenu("Color");
        JMenu helpMenu = new JMenu("Help");

        // Menu item in the first menu in the menu bar
        JMenuItem openTaskManagerItem = new JMenuItem("Open Task Manager");
        openTaskManagerItem.addActionListener(new taskManagerAction());

        // Menu items in the second menu in the menu bar
        JMenuItem openEmail = new JMenuItem("Email");
        openEmail.addActionListener(new emailAction());
        JMenuItem openPhoneNumber = new JMenuItem("Phone Number");
        openPhoneNumber.addActionListener(new phoneAction());
        JMenuItem openTwitter = new JMenuItem("Twitter");
        openTwitter.addActionListener(new twitterAction());

        // Menu items for Font
        JMenu changeFontItem = new JMenu("Change Font");

        fontMenu.add(fmItem1);
        fontMenu.add(fmItem2);
        fontMenu.add(fmItem3);
        fontMenu.add(fmItem4);
        fontMenu.add(fmItem5);
        fontMenu.addSeparator();

        changeFontItem.addActionListener(new FontStyleAction());
        fontMenu.add(changeFontItem);
        changeFontItem.add(rb1);
        changeFontItem.add(rb2);

        // Menu items for Color
        JMenuItem changeColorItem = new JMenuItem("Change Text Color");
        changeColorItem.addActionListener(new colorPickerAction());
        backgroundColorMenu.add(changeColorItem);

        // Adding menu items to menus then menus to menu bar
        taskManagerMenu.add(openTaskManagerItem);
        menuBar.add(taskManagerMenu);
        menuBar.add(backgroundColorMenu);
        menuBar.add(helpMenu);

        helpMenu.add(openEmail);
        helpMenu.add(openPhoneNumber);
        helpMenu.add(openTwitter);
        helpMenu.addActionListener(new helpMenuAction());
        fontMenu.add(changeFontItem);
        backgroundColorMenu.add(changeColorItem);
        rb1.addActionListener(new FontStyleAction());
        rb2.addActionListener(new FontStyleAction());
        fmItem1.addActionListener(new FontNameAction());
        fmItem2.addActionListener(new FontNameAction());
        fmItem3.addActionListener(new FontNameAction());
        fmItem4.addActionListener(new FontNameAction());
        fmItem5.addActionListener(new FontNameAction());

        //adding panels and menubar to frame
        super.add(navigationPanel, BorderLayout.NORTH);
        super.add(fileOperationPanel, BorderLayout.SOUTH);
        super.add(calendarPanel, BorderLayout.CENTER);
        super.add(taskPanel, BorderLayout.WEST);

        super.setJMenuBar(menuBar);

        tasks = new String[42]; // 6 rows * 7 columns for tasks (1 per day)
        initializeTasksArray();

        taskManagerMenuBar.add(fontMenu);
        taskManagerMenuBar.add(backgroundColorMenu);

        super.setVisible(true); // Display the calendar interface

    }

    private class prevMonth implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentCalendar.add(Calendar.MONTH, -1); // Decrementing month by 1
            updateCalendar(); // Update calendar display for the new month
        }
    }

    private class nextMonth implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentCalendar.add(Calendar.MONTH, 1); // Incrementing month by 1
            updateCalendar(); // Update calendar display for the new month
        }
    }

    private class loadAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int status = fileChooser.showOpenDialog(null);

            if (status == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try (Scanner scanner = new Scanner(selectedFile)) {
                    StringBuilder loadedTasks = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        loadedTasks.append(scanner.nextLine()).append("\n");
                    }

                    // Assuming you want to display the loaded tasks somewhere (modify as needed)
                    JTextArea taskTextArea = new JTextArea(loadedTasks.toString());
                    JOptionPane.showMessageDialog(null, new JScrollPane(taskTextArea),"Loaded Tasks form File",JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error loading file: " + ex.getMessage());
                }
            }
        }
    }

    private class saveAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int status = fileChooser.showSaveDialog(null);

            if (status == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try (PrintWriter printWriter = new PrintWriter(selectedFile)) {
                    // Assuming you want to save the tasks from the taskTextArea (modify as needed)
                    printWriter.write(taskTextArea.getText());
                    JOptionPane.showMessageDialog(null, "File saved successfully: " + selectedFile.getAbsolutePath());
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
                }
            }
        }
    }

    public class addTaskAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JFrame addTaskFrame = new JFrame();
            addTaskFrame.setTitle("Add Task");
            addTaskFrame.setSize(350, 250);
            addTaskFrame.setLocationRelativeTo(null);

            JPanel date = new JPanel();
            date.add(dateLabel);
            date.add(dayTF);
            date.add(monthTF);
            date.add(yearTF);

            JButton addButton = new JButton("Add task");
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    taskTextArea.append(taskTF.getText() + " - " + dayTF.getText() + "/" + monthTF.getText() + "/" + yearTF.getText() + "\n");
                    addTaskFrame.dispose();
                }
            });

            addTaskFrame.add(date, BorderLayout.NORTH);
            addTaskFrame.add(taskTF, BorderLayout.CENTER);
            addTaskFrame.add(addButton, BorderLayout.SOUTH);
            addTaskFrame.add(taskLabel, BorderLayout.WEST);
            addTaskFrame.pack();
            addTaskFrame.setVisible(true);
        }
    }

    private class colorPickerAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Color selectedColor = JColorChooser.showDialog(null, "Select Text Color", taskTextArea.getForeground());

            if (selectedColor != null) {
                taskTextArea.setForeground(selectedColor);
            }
        }
    }

    private class FontStyleAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!rb1.isSelected() && !rb2.isSelected()) {
                taskTextArea.setFont(new Font(taskTextArea.getFont().getFontName(), Font.PLAIN, taskTextArea.getFont().getSize()));
            }

            if (rb1.isSelected()) {
                taskTextArea.setFont(new Font(taskTextArea.getFont().getFontName(), Font.BOLD, taskTextArea.getFont().getSize()));
            }

            if (!rb2.isSelected()) {
            } else {
                taskTextArea.setFont(new Font(taskTextArea.getFont().getFontName(), Font.ITALIC, taskTextArea.getFont().getSize()));
            }

            if (rb1.isSelected() && rb2.isSelected()) {
                taskTextArea.setFont(new Font(taskTextArea.getFont().getFontName(), Font.BOLD + Font.ITALIC, taskTextArea.getFont().getSize()));
            }
        }
    }

    private class FontNameAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == fmItem1) {
                Font f = new Font("Dialog", taskTextArea.getFont().getStyle(), taskTextArea.getFont().getSize());
                taskTextArea.setFont(f);
            } else if (e.getSource() == fmItem2) {
                taskTextArea.setFont(new Font("DialogInput", taskTextArea.getFont().getStyle(), taskTextArea.getFont().getSize()));
            } else if (e.getSource() == fmItem3) {
                taskTextArea.setFont(new Font("Monospaced", taskTextArea.getFont().getStyle(), taskTextArea.getFont().getSize()));
            } else if (e.getSource() == fmItem4) {
                taskTextArea.setFont(new Font("SansSerif", taskTextArea.getFont().getStyle(), taskTextArea.getFont().getSize()));
            } else if (e.getSource() == fmItem5) {
                taskTextArea.setFont(new Font("Serif", taskTextArea.getFont().getStyle(), taskTextArea.getFont().getSize()));
            }
        }
    }

    private class helpMenuAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "hello");
        }
    }

    private class emailAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "calendarHelp@calendar.com","Email",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class phoneAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "+9965346427","Phone Number",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class twitterAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "www.twitter.com/calendarHelp","Twitter",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class taskManagerAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame taskManagerFrame = new JFrame("Task Manager");
            taskManagerFrame.setSize(300, 230);

            JButton closeTaskManagerButton = new JButton("Close Task Manager");
            JMenu taskTextFont = new JMenu();
            taskTextFont.addActionListener(new colorPickerAction());
            JMenu taskTextColor = new JMenu();

            closeTaskManagerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    taskManagerFrame.dispose();
                }
            });

            JPanel taskManagerPanel = new JPanel(new BorderLayout());
            taskManagerPanel.add(new JScrollPane(taskTextArea), BorderLayout.CENTER);
            taskManagerPanel.add(closeTaskManagerButton, BorderLayout.SOUTH);

            taskManagerFrame.setJMenuBar(taskManagerMenuBar);

            taskManagerFrame.add(taskManagerPanel);
            taskManagerFrame.setLocationRelativeTo(null);
            taskManagerFrame.setVisible(true);

        }
    }

    private void updateCalendar() {
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int month = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);

        monthLabel.setText(new java.text.SimpleDateFormat("MMMM yyyy").format(currentCalendar.getTime()));

        Calendar cloneCalendar = (Calendar) currentCalendar.clone();
        int startDay = cloneCalendar.get(Calendar.DAY_OF_WEEK);
        cloneCalendar.add(Calendar.DAY_OF_MONTH, -startDay + 1);

        for (int i = 0; i < 42; i++) {
            if (dayButtons[i] != null) {
                calendarPanel.remove(dayButtons[i]);
            }

            dayButtons[i] = new JButton();
            dayButtons[i].setText(Integer.toString(cloneCalendar.get(Calendar.DAY_OF_MONTH)));
            dayButtons[i].setForeground(Color.black);
            dayButtons[i].setBackground(Color.white);

            int finalI = i;
            dayButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, dayButtons[finalI].getText() + "/" + (currentCalendar.get(Calendar.MONTH) + 1) + "/" + currentCalendar.get(Calendar.YEAR) + tasks[finalI]);
                    // PLUS 1 because index of months start at 0
                }
            });

            if (cloneCalendar.get(Calendar.MONTH) != month) {
                dayButtons[i].setEnabled(false);
                dayButtons[i].setForeground(Color.blue);
            }

            calendarPanel.add(dayButtons[i]);
            cloneCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    // Method to initialize the tasks array
    private void initializeTasksArray() {
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = ""; // Initialize all tasks as empty initially
        }
    }

    public static void main(String[] args) {
        new OOPProject();
    }
}
