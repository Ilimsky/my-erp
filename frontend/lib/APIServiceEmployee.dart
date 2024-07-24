import 'dart:convert';
import 'package:http/http.dart' as http;

import 'Employee.dart';

class APIServiceEmployee {
  final String baseUrl;

  APIServiceEmployee({required this.baseUrl});

  Future<List<Employee>> fetchEmployees() async {
    final response = await http.get(Uri.parse('$baseUrl/employees'));
    if (response.statusCode == 200) {
      List<dynamic> jsonResponse = jsonDecode(response.body);
      // Логирование данных
      print(jsonResponse);
      return jsonResponse.map((employee) => Employee.fromJson(employee)).toList();
    } else {
      throw Exception('Failed to load employees');
    }
  }
}
