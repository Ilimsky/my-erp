import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'Department.dart';

class DepartmentList extends StatelessWidget {
  final List<Department> departments;

  DepartmentList({required this.departments});

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: departments.length,
      itemBuilder: (context, index) {
        return ListTile(
          title: Text(departments[index].name),
        );
      },
    );
  }
}
