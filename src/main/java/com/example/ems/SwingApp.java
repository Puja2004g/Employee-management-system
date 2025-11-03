package com.example.ems;

import com.example.ems.ui.EmployeeManagementFrame;
import com.example.ems.ui.EmploymentRecordFrame;
import com.example.ems.util.HibernateUtil;
import javax.swing.JOptionPane;

public class SwingApp {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
        showModuleChooser();
    }

    public static void showModuleChooser() {
        int choice = JOptionPane.showOptionDialog(
                null,
                "What would you like to manage?",
                "Select Module",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Employees", "Employment Records"},
                "Employees");

        if (choice == 1) {
            EmploymentRecordFrame.showUi();
        } else {
            EmployeeManagementFrame.showUi();
        }
    }
}


