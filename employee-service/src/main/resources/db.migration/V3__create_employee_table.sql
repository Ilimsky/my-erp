//V3__create_employee_table.sql

CREATE TABLE employee
(
    employee_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_name   VARCHAR(255) NOT NULL,
    department_id   BIGINT,
    department_name VARCHAR(255)
);
