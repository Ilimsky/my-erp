import 'dart:convert';
import 'package:http/http.dart' as http;

import 'Employee.dart';
import 'EmployeeDTO.dart';

class APIService {
  final String baseUrl;
  final http.Client client;

  APIService({this.baseUrl = 'http://127.0.0.1:8080', required this.client});

  Future<List<Employee>> getAllEmployees() async {
    final response = await client.get(Uri.parse('$baseUrl/employees'));

    if (response.statusCode == 200) {
      final List<dynamic> jsonList = jsonDecode(response.body);
      print('Response data: $jsonList'); // Отладочное сообщение
      return jsonList.map((dynamic item) => Employee.fromJson(item)).toList();
    } else {
      throw Exception('Failed to fetch employees');
    }
  }

  Future<Employee> createEmployee(EmployeeDTO employeeDTO) async {
    // Remove ID from the request body
    final body = jsonEncode({
      'name': employeeDTO.name,
    });
    print('Request body: $body'); // Debug message

    try {
      final response = await client.post(
        Uri.parse('$baseUrl/employee'),
        headers: {'Content-Type': 'application/json'},
        body: body,
      );

      print('Response status: ${response.statusCode}'); // Debug message
      print('Response body: ${response.body}'); // Debug message

      if (response.statusCode == 200) {
        final dynamic json = jsonDecode(response.body);
        if (json != null) {
          return Employee.fromJson(json);
        } else {
          throw Exception('Received null response');
        }
      } else {
        throw Exception('Failed to create employee. Status code: ${response.statusCode}, Body: ${response.body}');
      }
    } catch (e) {
      print('Error during HTTP request: $e'); // Debug message
      rethrow; // Ensure the error is propagated
    }
  }

  Future<Employee> getEmployeeById(int id) async {
    final response = await client.get(Uri.parse('$baseUrl/employee/$id'));

    if (response.statusCode == 200) {
      final dynamic json = jsonDecode(response.body);
      if (json != null) {
        return Employee.fromJson(json);
      } else {
        throw Exception('Received null response');
      }
    } else {
      throw Exception('Failed to fetch employee');
    }
  }

  Future<void> updateEmployee(int id, EmployeeDTO employeeDTO) async {
    final body = jsonEncode(employeeDTO.toJson());
    final response = await client.put(Uri.parse('$baseUrl/employee/$id'),
        headers: {'Content-Type': 'application/json'}, body: body);

    if (response.statusCode != 200) {
      throw Exception('Failed to update employee');
    }
  }

  Future<void> deleteEmployee(int id) async {
    final response = await client.delete(Uri.parse('$baseUrl/employee/$id'));

    if (response.statusCode != 204) {
      throw Exception('Failed to delete employee');
    }
  }
}
