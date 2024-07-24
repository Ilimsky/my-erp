import 'package:flutter/material.dart';

import 'APIServiceDepartment.dart';
import 'APIServiceEmployee.dart';
import 'Department.dart';
import 'DepartmentList.dart';
import 'Employee.dart';
import 'EmployeeList.dart';

class HomeScreen extends StatelessWidget {
  final APIServiceEmployee apiServiceEmployee = APIServiceEmployee(baseUrl: 'http://localhost:8080');
  final APIServiceDepartment apiServiceDepartment = APIServiceDepartment(baseUrl: 'http://localhost:8081');

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Employee and Department Management'),
      ),
      body: FutureBuilder(
        future: Future.wait([apiServiceEmployee.fetchEmployees(), apiServiceDepartment.fetchDepartments()]),
        builder: (context, AsyncSnapshot<List<dynamic>> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else {
            List<Employee> employees = snapshot.data![0];
            List<Department> departments = snapshot.data![1];

            // Логирование данных
            print(employees);

            return Column(
              children: [
                Expanded(child: EmployeeList(employees: employees)),
                // Expanded(child: DepartmentList(departments: departments)),
              ],
            );
          }
        },
      ),
    );
  }
}
