package com.example.ems;

import com.example.ems.ui.EmployeeManagementFrame;
import com.example.ems.util.HibernateUtil;

public class SwingApp {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
        EmployeeManagementFrame.showUi();
    }
}


