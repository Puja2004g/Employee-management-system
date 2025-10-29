package com.example.ems;

import com.example.ems.entity.Employee;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.util.HibernateUtil;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeRepository repo = new EmployeeRepository();

    public static void main(String[] args) {
        try {
            runMenu();
        } finally {
            HibernateUtil.shutdown();
        }
    }

    private static void runMenu() {
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> updateEmployee();
                case 3 -> deleteEmployee();
                case 4 -> showEmployeeById();
                case 5 -> listAllEmployees();
                case 6 -> findEmployeesByName();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("==== Employee Management System ====");
        System.out.println("1) Add new employee");
        System.out.println("2) Update existing employee");
        System.out.println("3) Delete an employee");
        System.out.println("4) Show employee details by ID");
        System.out.println("5) Show all employee details");
        System.out.println("6) Find employees by name");
        System.out.println("0) Exit");
    }

    private static void addEmployee() {
        System.out.println("-- Add Employee --");
        String firstName = readLine("First name: ");
        String lastName = readLine("Last name: ");
        String email = readLine("Email: ");
        String department = readLine("Department: ");

        Employee e = new Employee(firstName, lastName, email, department);
        try {
            repo.save(e);
            System.out.println("Saved: " + e);
        } catch (RuntimeException ex) {
            System.out.println("Error saving employee: " + ex.getMessage());
        }
    }

    private static void updateEmployee() {
        System.out.println("-- Update Employee --");
        Long id = (long) readInt("Employee ID to update: ");
        Employee existing = repo.findById(id);
        if (existing == null) {
            System.out.println("Employee not found.");
            return;
        }
        System.out.println("Current: " + existing);
        String firstName = readLine("First name [" + existing.getFirstName() + "]: ");
        String lastName = readLine("Last name [" + existing.getLastName() + "]: ");
        String email = readLine("Email [" + existing.getEmail() + "]: ");
        String department = readLine("Department [" + existing.getDepartment() + "]: ");

        if (!firstName.isBlank()) existing.setFirstName(firstName);
        if (!lastName.isBlank()) existing.setLastName(lastName);
        if (!email.isBlank()) existing.setEmail(email);
        if (!department.isBlank()) existing.setDepartment(department);

        try {
            repo.update(existing);
            System.out.println("Updated: " + existing);
        } catch (RuntimeException ex) {
            System.out.println("Error updating employee: " + ex.getMessage());
        }
    }

    private static void deleteEmployee() {
        System.out.println("-- Delete Employee --");
        Long id = (long) readInt("Employee ID to delete: ");
        Employee existing = repo.findById(id);
        if (existing == null) {
            System.out.println("Employee not found.");
            return;
        }
        repo.delete(id);
        System.out.println("Deleted employee with ID: " + id);
    }

    private static void showEmployeeById() {
        System.out.println("-- Show Employee by ID --");
        Long id = (long) readInt("Employee ID: ");
        Employee e = repo.findById(id);
        if (e == null) {
            System.out.println("Employee not found.");
        } else {
            System.out.println(e);
        }
    }

    private static void listAllEmployees() {
        System.out.println("-- All Employees --");
        List<Employee> employees = repo.findAll();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee e : employees) {
                System.out.println(e);
            }
        }
    }

    private static void findEmployeesByName() {
        System.out.println("-- Find Employees by Name --");
        String query = readLine("Name contains: ");
        List<Employee> employees = repo.findByName(query);
        if (employees.isEmpty()) {
            System.out.println("No matching employees.");
        } else {
            for (Employee e : employees) {
                System.out.println(e);
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}


