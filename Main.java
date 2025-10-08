import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    // JDBC Database URL
    static final String DB_URL = "jdbc:sqlite:C:/Users/thiru/OneDrive/Desktop/mydatabases/student_database.db";
    
    // Create the PDF documents table
    static final String CREATE_PDF_TABLE = "CREATE TABLE IF NOT EXISTS pdf_documents (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "file_data BLOB)";

    public static void main(String[] args) {
        // Create the table if it doesn't exist
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_PDF_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create the login page
        SwingUtilities.invokeLater(() -> createLoginPage());
    }

    

    private static void createLoginPage() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, frame);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, JFrame loginFrame) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 135, 25);
        panel.add(passwordText);

        // Show password checkbox
        JCheckBox showPasswordCheckBox = new JCheckBox("Show");
        showPasswordCheckBox.setBounds(240, 50, 60, 25);
        panel.add(showPasswordCheckBox);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        // ActionListener for the login button
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            // Check credentials
            if (isValidUser(username, password)) {
                // If credentials are valid, open the main application window
                openMainApplication(loginFrame);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid login credentials.. Please! Enter your RollNumber...");
            }
        });

        // ActionListener for the show password checkbox
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                // Show the password
                passwordText.setEchoChar((char) 0);
            } else {
                // Hide the password
                passwordText.setEchoChar('*');
            }
        });
    }

    private static boolean isValidUser(String username, String password) {
        // Check if the username follows the specified pattern
        if (!username.matches("[a-z]*")) {
            return false;
        }

        // Check if the password follows the specified pattern
        if (!password.matches("2211cs010\\d{3}")) {
            return false;
        }

        return true;
    }

    private static void openMainApplication(JFrame loginFrame) {
        // Close the login frame
        loginFrame.dispose();

        // Create the main application frame
        JFrame mainFrame = new JFrame("eNOTES");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);

        // Create a panel for the main application
        JPanel mainPanel = new JPanel();
        mainFrame.add(mainPanel);
        placeMainComponents(mainPanel);

        // Show the main application frame
        mainFrame.setVisible(true);

        // Add a window listener to perform actions when the main frame is closed
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Perform cleanup or additional actions if needed
                System.out.println("Closing application");
            }
        });
    }

    private static void placeMainComponents(JPanel mainPanel) {
        mainPanel.setLayout(null);

        JButton studentDetailsButton = new JButton("Student Details");
        studentDetailsButton.setBounds(10, 20, 150, 25);
        mainPanel.add(studentDetailsButton);

        JButton uploadPdfButton = new JButton("Upload PDF");
        uploadPdfButton.setBounds(10, 60, 150, 25);
        mainPanel.add(uploadPdfButton);

        JButton viewPdfButton = new JButton("View PDF");
        viewPdfButton.setBounds(10, 100, 150, 25);
        mainPanel.add(viewPdfButton);

        // ActionListener for the student details button
        studentDetailsButton.addActionListener(e -> {
            // Open a new window for student details
            openStudentDetailsWindow();
        });

        // ActionListener for the upload PDF button
        uploadPdfButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // Perform the upload to the database
                uploadPdfToDatabase(selectedFile);
                JOptionPane.showMessageDialog(null, "PDF uploaded successfully");
            }
        });

        // ActionListener for the view PDF button
        viewPdfButton.addActionListener(e -> {
            // Fetch and display the uploaded PDFs
            showUploadedPDFs();
        });

        JButton exitButton = new JButton("Exit");  // Added Exit button
        exitButton.setBounds(10, 140, 150, 25);
        mainPanel.add(exitButton);

        // ActionListener for the exit button
        exitButton.addActionListener(e -> {
            // Close the main frame and exit the application
            System.exit(0);
        });
    }

    private static void openStudentDetailsWindow() {
        JFrame studentDetailsFrame = new JFrame("Student Details");
        studentDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studentDetailsFrame.setSize(600, 400);
        studentDetailsFrame.setLocationRelativeTo(null);

        // Create a panel for student details
        JPanel studentDetailsPanel = new JPanel();
        studentDetailsFrame.add(studentDetailsPanel);
        placeStudentDetailsComponents(studentDetailsPanel);

        // Show the student details frame
        studentDetailsFrame.setVisible(true);
    }

    private static void placeStudentDetailsComponents(JPanel studentDetailsPanel) {
        studentDetailsPanel.setLayout(new GridLayout(3, 1));

        JButton examinationRecordsButton = new JButton("Examination Records");
        JButton attendanceRecordsButton = new JButton("Attendance Records");
        JButton disciplineRecordsButton = new JButton("Discipline Records");
        JButton displayButton = new JButton("Display");

        studentDetailsPanel.add(examinationRecordsButton);
        studentDetailsPanel.add(attendanceRecordsButton);
        studentDetailsPanel.add(disciplineRecordsButton);
        studentDetailsPanel.add(displayButton);

        // ActionListener for examination records button
        examinationRecordsButton.addActionListener(e -> {
            // Open a new window for examination records
            openExaminationRecordsWindow();
        });

        // ActionListener for attendance records button
        attendanceRecordsButton.addActionListener(e -> {
            // Open a new window for attendance records
            openAttendanceRecordsWindow();
        });

        // ActionListener for discipline records button
        disciplineRecordsButton.addActionListener(e -> {
            // Open a new window for discipline records
            openDisciplineRecordsWindow();
        });

        // ActionListener for display button
        displayButton.addActionListener(e -> {
            // Display the student details
            displayStudentDetails();
        });
    }

    private static void openExaminationRecordsWindow() {
        JFrame examinationRecordsFrame = new JFrame("Examination Records");
        examinationRecordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        examinationRecordsFrame.setSize(600, 400);
        examinationRecordsFrame.setLocationRelativeTo(null);

        // Create a panel for examination records
        JPanel examinationRecordsPanel = new JPanel();
        examinationRecordsFrame.add(examinationRecordsPanel);
        placeExaminationRecordsComponents(examinationRecordsPanel);

        // Show the examination records frame
        examinationRecordsFrame.setVisible(true);
    }

    private static void placeExaminationRecordsComponents(JPanel examinationRecordsPanel) {
        examinationRecordsPanel.setLayout(new GridLayout(3, 1));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel marksLabel = new JLabel("Marks:");
        JTextField marksField = new JTextField();
        JButton saveButton = new JButton("Save");

        examinationRecordsPanel.add(nameLabel);
        examinationRecordsPanel.add(nameField);
        examinationRecordsPanel.add(marksLabel);
        examinationRecordsPanel.add(marksField);
        examinationRecordsPanel.add(saveButton);

        // ActionListener for the save button
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String marks = marksField.getText();

            // Save the examination record to the database
            saveExaminationRecord(name, marks);

            // Clear the input fields
            nameField.setText("");
            marksField.setText("");

            JOptionPane.showMessageDialog(null, "Examination record saved successfully");
        });
    }

    private static void saveExaminationRecord(String name, String marks) {
        // Save the examination record to the Excel file
        try (FileWriter writer = new FileWriter("student_details.xlsx", true)) {
            writer.write("Name: " + name + "  ," + "   " + "Marks: " + marks + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving examination record");
        }
    }

    private static void openAttendanceRecordsWindow() {
        JFrame attendanceRecordsFrame = new JFrame("Attendance Records");
        attendanceRecordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        attendanceRecordsFrame.setSize(600, 400);
        attendanceRecordsFrame.setLocationRelativeTo(null);

        // Create a panel for attendance records
        JPanel attendanceRecordsPanel = new JPanel();
        attendanceRecordsFrame.add(attendanceRecordsPanel);
        placeAttendanceRecordsComponents(attendanceRecordsPanel);

        // Show the attendance records frame
        attendanceRecordsFrame.setVisible(true);
    }

    private static void placeAttendanceRecordsComponents(JPanel attendanceRecordsPanel) {
        attendanceRecordsPanel.setLayout(new GridLayout(3, 1));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel attendanceLabel = new JLabel("Attendance:");
        JTextField attendanceField = new JTextField();
        JButton saveButton = new JButton("Save");

        attendanceRecordsPanel.add(nameLabel);
        attendanceRecordsPanel.add(nameField);
        attendanceRecordsPanel.add(attendanceLabel);
        attendanceRecordsPanel.add(attendanceField);
        attendanceRecordsPanel.add(saveButton);

        // ActionListener for the save button
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String attendance = attendanceField.getText();

            // Save the attendance record to the Excel file
            try (FileWriter writer = new FileWriter("student_details.xlsx", true)) {
                writer.write("Name: "  +  name + "  ," + "   " +"Attendence: " + attendance + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving attendance record");
            }

            // Clear the input fields
            nameField.setText("");
            attendanceField.setText("");

            JOptionPane.showMessageDialog(null, "Attendance record saved successfully");
        });
    }

    private static void openDisciplineRecordsWindow() {
        JFrame disciplineRecordsFrame = new JFrame("Discipline Records");
        disciplineRecordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        disciplineRecordsFrame.setSize(600, 400);
        disciplineRecordsFrame.setLocationRelativeTo(null);

        // Create a panel for discipline records
        JPanel disciplineRecordsPanel = new JPanel();
        disciplineRecordsFrame.add(disciplineRecordsPanel);
        placeDisciplineRecordsComponents(disciplineRecordsPanel);

        // Show the discipline records frame
        disciplineRecordsFrame.setVisible(true);
    }

    private static void placeDisciplineRecordsComponents(JPanel disciplineRecordsPanel) {
        disciplineRecordsPanel.setLayout(new GridLayout(3, 1));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel disciplineLabel = new JLabel("Discipline:");
        JTextField disciplineField = new JTextField();
        JButton saveButton = new JButton("Save");

        disciplineRecordsPanel.add(nameLabel);
        disciplineRecordsPanel.add(nameField);
        disciplineRecordsPanel.add(disciplineLabel);
        disciplineRecordsPanel.add(disciplineField);
        disciplineRecordsPanel.add(saveButton);

        // ActionListener for the save button
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String discipline = disciplineField.getText();

            // Save the discipline record to the Excel file
            try (FileWriter writer = new FileWriter("student_details.xlsx", true)) {
                writer.write("Name: " + name + "  ," + "   "+ "Discipline: " + discipline + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving discipline record");
            }

            // Clear the input fields
            nameField.setText("");
            disciplineField.setText("");

            JOptionPane.showMessageDialog(null, "Discipline record saved successfully");
        });
    }

    private static void uploadPdfToDatabase(File file) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            // Assuming you have a table named 'pdf_documents' with columns 'id' and 'file_data'
            String sql = "INSERT INTO pdf_documents (file_data) VALUES (?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set the file data as a blob
                preparedStatement.setBytes(1, Files.readAllBytes(file.toPath()));

                // Execute the query
                preparedStatement.executeUpdate();
            }

            System.out.println("PDF uploaded to the database successfully");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void showUploadedPDFs() {
        JFrame pdfFrame = new JFrame("Uploaded PDFs");
        pdfFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pdfFrame.setSize(400, 300);
        pdfFrame.setLocationRelativeTo(null);

        JPanel pdfPanel = new JPanel();
        pdfFrame.add(pdfPanel);
        pdfPanel.setLayout(new BoxLayout(pdfPanel, BoxLayout.Y_AXIS));

        // Fetch PDFs from the database
        fetchPDFsFromDatabase().forEach(pdfFile -> {
            JButton pdfButton = new JButton(pdfFile.getName());
            pdfPanel.add(pdfButton);

            // ActionListener for each PDF button
            pdfButton.addActionListener(event -> viewPdf(pdfFile));
        });

        pdfFrame.setVisible(true);
    }


    private static List<File> fetchPDFsFromDatabase() {
        List<File> pdfList = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT file_data FROM pdf_documents";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
    
                while (resultSet.next()) {
                    // Retrieve the PDF data as bytes
                    byte[] pdfData = resultSet.getBytes("file_data");
    
                    // Save the PDF data to a temporary file
                    File tempFile = File.createTempFile("temp", ".pdf");
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        fos.write(pdfData);
                    }
    
                    pdfList.add(tempFile);
                }
    
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error creating temporary PDF file: " + e.getMessage());
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching PDFs from the database: " + e.getMessage());
        }
    
        return pdfList;
    }
    

    private static void viewPdf(File file) {
        // Open the PDF using the default system viewer
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void displayStudentDetails() {
        try (FileReader reader = new FileReader("student_details.xlsx")) {
            int character;
            while ((character = reader.read()) != -1) {
                System.out.print((char) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error displaying student details");
        }
    }
}