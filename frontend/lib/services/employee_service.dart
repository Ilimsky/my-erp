import 'dart:convert';
import 'package:http/http.dart' as http;

import '../models/employee.dart';
// import 'package:employee_app/models/employee.dart';

class EmployeeService {
  final String baseUrl = "http://localhost:8080";

  Future<Employee> createEmployee(Employee employee) async {
    final response = await http.post(
      Uri.parse('$baseUrl/employee'),
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(employee.toJson()),
    );

    if (response.statusCode == 200) {
      return Employee.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to create employee');
    }
  }
}
