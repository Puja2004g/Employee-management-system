package com.example.ems.ui;

import com.example.ems.entity.Employee;
import com.example.ems.repository.EmployeeRepository;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

public class EmployeeManagementFrame extends JFrame {
    private final EmployeeRepository repository = new EmployeeRepository();

    private final JTextField idField = new JTextField(10);
    private final JTextField firstNameField = new JTextField(15);
    private final JTextField lastNameField = new JTextField(15);
    private final JTextField emailField = new JTextField(20);
    private final JTextField departmentField = new JTextField(15);
    private final JTextField searchNameField = new JTextField(15);
    private final JTextField searchIdField = new JTextField(8);

    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "First Name", "Last Name", "Email", "Department"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public EmployeeManagementFrame() {
        super("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUi();
        setSize(900, 600);
        setLocationRelativeTo(null);
        refreshTable();
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.add(buildFormPanel(), BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        root.add(buildActionsPanel(), BorderLayout.SOUTH);
        setContentPane(root);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                    firstNameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                    lastNameField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                    emailField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                    departmentField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                }
            }
        });
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        c.gridx = 0; c.gridy = y; panel.add(new JLabel("ID:"), c);
        c.gridx = 1; idField.setEditable(false); panel.add(idField, c);

        y++; c.gridx = 0; c.gridy = y; panel.add(new JLabel("First Name:"), c);
        c.gridx = 1; panel.add(firstNameField, c);

        y++; c.gridx = 0; c.gridy = y; panel.add(new JLabel("Last Name:"), c);
        c.gridx = 1; panel.add(lastNameField, c);

        y++; c.gridx = 0; c.gridy = y; panel.add(new JLabel("Email:"), c);
        c.gridx = 1; panel.add(emailField, c);

        y++; c.gridx = 0; c.gridy = y; panel.add(new JLabel("Department:"), c);
        c.gridx = 1; panel.add(departmentField, c);

        return panel;
    }

    private JPanel buildActionsPanel() {
        JPanel panel = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton findByNameBtn = new JButton("Find by Name");
        JButton findByIdBtn = new JButton("Find by ID");

        panel.add(new JLabel("Search Name:"));
        panel.add(searchNameField);
        panel.add(findByNameBtn);
        panel.add(new JLabel("Search ID:"));
        panel.add(searchIdField);
        panel.add(findByIdBtn);
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);

        addBtn.addActionListener(e -> addEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        refreshBtn.addActionListener(e -> refreshTable());
        findByNameBtn.addActionListener(e -> findByName());
        findByIdBtn.addActionListener(e -> findById());

        return panel;
    }

    private void refreshTable() {
        List<Employee> employees = repository.findAll();
        renderTable(employees);
    }

    private void renderTable(List<Employee> employees) {
        tableModel.setRowCount(0);
        for (Employee e : employees) {
            tableModel.addRow(new Object[]{e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getDepartment()});
        }
    }

    private void addEmployee() {
        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String dept = departmentField.getText().trim();
        if (first.isEmpty() || last.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name, last name, and email are required.");
            return;
        }
        try {
            Employee saved = repository.save(new Employee(first, last, email, dept));
            JOptionPane.showMessageDialog(this, "Saved employee with ID: " + saved.getId());
            clearForm();
            refreshTable();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an employee in the table to update.");
            return;
        }
        Long id = Long.valueOf(idText);
        Employee existing = repository.findById(id);
        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }
        existing.setFirstName(firstNameField.getText().trim());
        existing.setLastName(lastNameField.getText().trim());
        existing.setEmail(emailField.getText().trim());
        existing.setDepartment(departmentField.getText().trim());
        try {
            repository.update(existing);
            JOptionPane.showMessageDialog(this, "Updated employee.");
            clearForm();
            refreshTable();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Error updating: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an employee in the table to delete.");
            return;
        }
        Long id = Long.valueOf(idText);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete employee ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                repository.delete(id);
                JOptionPane.showMessageDialog(this, "Deleted employee.");
                clearForm();
                refreshTable();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting: " + ex.getMessage());
            }
        }
    }

    private void findByName() {
        String q = searchNameField.getText().trim();
        List<Employee> employees = q.isEmpty() ? repository.findAll() : repository.findByName(q);
        renderTable(employees);
    }

    private void findById() {
        String idText = searchIdField.getText().trim();
        if (idText.isEmpty()) {
            refreshTable();
            return;
        }
        try {
            Long id = Long.valueOf(idText);
            Employee e = repository.findById(id);
            if (e != null) {
                renderTable(List.of(e));
            } else {
                renderTable(List.of());
                JOptionPane.showMessageDialog(this, "No employee with ID: " + id);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }

    private void clearForm() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        departmentField.setText("");
    }

    public static void showUi() {
        SwingUtilities.invokeLater(() -> new EmployeeManagementFrame().setVisible(true));
    }
}


