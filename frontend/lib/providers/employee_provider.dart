import 'package:flutter/material.dart';

import '../models/employee.dart';
import '../services/employee_service.dart';
// import 'package:employee_app/models/employee.dart';
// import 'package:employee_app/services/employee_service.dart';

class EmployeeProvider with ChangeNotifier {
  EmployeeService _employeeService = EmployeeService();
  Employee? _employee;

  Employee? get employee => _employee;

  Future<void> createEmployee(Employee employee) async {
    _employee = await _employeeService.createEmployee(employee);
    notifyListeners();
  }
}
