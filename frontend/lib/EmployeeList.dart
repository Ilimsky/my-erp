import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'Employee.dart';

class EmployeeList extends StatelessWidget {
  final List<Employee> employees;

  EmployeeList({required this.employees});

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: employees.length,
      itemBuilder: (context, index) {
        return ListTile(
          title: Text(employees[index].name),
          subtitle: Text('Department: ${employees[index].departmentId}'),
        );
      },
    );
  }
}
