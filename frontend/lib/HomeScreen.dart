import 'package:flutter/material.dart';
import 'APIServiceEmployee.dart';
import 'APIServiceDepartment.dart';

class HomeScreen extends StatelessWidget {
  final APIServiceEmployee apiServiceEmployee = APIServiceEmployee(baseUrl: 'http://localhost:8080');
  final APIServiceDepartment apiServiceDepartment = APIServiceDepartment(baseUrl: 'http://localhost:8081');

  HomeScreen({super.key});

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
            List<dynamic> employees = snapshot.data![0];
            List<dynamic> departments = snapshot.data![1];

            return Column(
              children: [
                Expanded(child: EmployeeList(employees: employees)),
                Expanded(child: DepartmentList(departments: departments)),
              ],
            );
          }
        },
      ),
    );
  }
}

class EmployeeList extends StatelessWidget {
  final List<dynamic> employees;

  EmployeeList({required this.employees});

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: employees.length,
      itemBuilder: (context, index) {
        return ListTile(
          title: Text(employees[index]['name']),
          subtitle: Text('Department: ${employees[index]['departmentId']}'),
        );
      },
    );
  }
}

class DepartmentList extends StatelessWidget {
  final List<dynamic> departments;

  DepartmentList({required this.departments});

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: departments.length,
      itemBuilder: (context, index) {
        return ListTile(
          title: Text(departments[index]['name']),
        );
      },
    );
  }
}
