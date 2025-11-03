package com.example.ems.ui;

import com.example.ems.entity.Employee;
import com.example.ems.entity.EmploymentRecord;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.repository.EmploymentRecordRepository;
import com.example.ems.SwingApp;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.Year;
import java.util.List;

public class EmploymentRecordFrame extends JFrame {
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final EmploymentRecordRepository recordRepository = new EmploymentRecordRepository();

    private final JTextField employeeIdField = new JTextField(10);
    private final JTextField firstNameField = new JTextField(12);
    private final JTextField lastNameField = new JTextField(12);
    private final JTextField startYearField = new JTextField(6);
    private final JCheckBox stillEmployedCheck = new JCheckBox("Still employed (end year = Present)", true);

    public EmploymentRecordFrame() {
        super("Add Employment Record");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildUi();
        setSize(520, 260);
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        c.gridx = 0; c.gridy = y; panel.add(new JLabel("Employee ID:"), c);
        c.gridx = 1; panel.add(employeeIdField, c);

        y++; c.gridx = 0; c.gridy = y; panel.add(new JLabel("First Name:"), c);
        c.gridx = 1; panel.add(firstNameField, c);

        y++; c.gridx = 0; c.gridy = y; panel.add(new JLabel("Last Name:"), c);
        c.gridx = 1; panel.add(lastNameField, c);

        y++; c.gridx = 0; c.gridy = y; panel.add(new JLabel("Start Year:"), c);
        c.gridx = 1; panel.add(startYearField, c);

        y++; c.gridx = 0; c.gridy = y; c.gridwidth = 2; panel.add(stillEmployedCheck, c);
        c.gridwidth = 1;

        JButton saveBtn = new JButton("Save Record");
        JButton backBtn = new JButton("Back");
        y++; c.gridx = 0; c.gridy = y; panel.add(saveBtn, c);
        c.gridx = 1; panel.add(backBtn, c);
        c.gridwidth = 1;

        setContentPane(panel);

        saveBtn.addActionListener(e -> onSave());
        backBtn.addActionListener(e -> onBack());
    }

    private void onSave() {
        Employee employee = resolveEmployee();
        if (employee == null) return;

        Integer startYear = parseStartYear();
        if (startYear == null) return;

        String endYear = stillEmployedCheck.isSelected() ? "Present" : String.valueOf(Year.now().getValue());

        EmploymentRecord record = new EmploymentRecord(
                employee,
                employee.getFirstName() + " " + employee.getLastName(),
                startYear,
                endYear
        );

        try {
            recordRepository.save(record);
            JOptionPane.showMessageDialog(this, "Employment record saved for employee ID: " + employee.getId());
            clearForm();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Error saving record: " + ex.getMessage());
        }
    }

    private Employee resolveEmployee() {
        String idText = employeeIdField.getText().trim();
        if (!idText.isEmpty()) {
            try {
                Long id = Long.valueOf(idText);
                Employee byId = employeeRepository.findById(id);
                if (byId == null) {
                    JOptionPane.showMessageDialog(this, "No employee found with ID: " + id);
                }
                return byId;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid employee ID");
                return null;
            }
        }

        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();
        if (first.isEmpty() || last.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Provide Employee ID or BOTH First and Last Name");
            return null;
        }

        // Exact match on first and last name (case-insensitive)
        List<Employee> matches = employeeRepository.findByFirstAndLast(first, last);
        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employee found with the exact name: " + first + " " + last);
            return null;
        }
        if (matches.size() > 1) {
            JOptionPane.showMessageDialog(this, "Multiple employees share that name. Please enter the Employee ID.");
            return null;
        }
        return matches.get(0);
    }

    private Integer parseStartYear() {
        String text = startYearField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Start year is required");
            return null;
        }
        try {
            int year = Integer.parseInt(text);
            int current = Year.now().getValue();
            if (year < 1900 || year > current) {
                JOptionPane.showMessageDialog(this, "Start year must be between 1900 and " + current);
                return null;
            }
            return year;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid start year");
            return null;
        }
    }

    private void clearForm() {
        employeeIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        startYearField.setText("");
        stillEmployedCheck.setSelected(true);
    }

    public static void showUi() {
        SwingUtilities.invokeLater(() -> new EmploymentRecordFrame().setVisible(true));
    }

    private void onBack() {
        dispose();
        SwingApp.showModuleChooser();
    }
}


