package com.example.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.time.Year;

@Entity
@Table(name = "employment_records")
public class EmploymentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false, length = 200)
    private String employeeName;

    // Start year of employment
    @Column(nullable = false)
    private Integer startYear;

    // End year of employment; null means still employed/current
    @Column
    private String endYear; // "Present" when still employed, otherwise a year number as string

    public EmploymentRecord() {
    }

    public EmploymentRecord(Employee employee, String employeeName, Integer startYear, String endYear) {
        this.employee = employee;
        this.employeeName = employeeName;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    /**
     * Returns how many full years the employee has worked, computed as
     * (currentYear or endYear) - startYear. Minimum of 0.
     */
    public int getYearsWorked() {
        int effectiveEndYear;
        if (endYear == null || "Present".equalsIgnoreCase(endYear)) {
            effectiveEndYear = Year.now().getValue();
        } else {
            try {
                effectiveEndYear = Integer.parseInt(endYear);
            } catch (NumberFormatException e) {
                effectiveEndYear = Year.now().getValue();
            }
        }
        return Math.max(0, effectiveEndYear - (startYear != null ? startYear : effectiveEndYear));
    }

    /**
     * Returns a human-readable period string, e.g., "2018 - 2025" or "2018 - Present".
     */
    public String getPeriod() {
        String end = (endYear == null || endYear.isBlank()) ? "Present" : endYear;
        return String.valueOf(startYear) + " - " + end;
    }
}


