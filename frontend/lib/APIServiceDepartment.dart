import 'dart:convert';
import 'package:http/http.dart' as http;

import 'Department.dart';

class APIServiceDepartment {
  final String baseUrl;

  APIServiceDepartment({required this.baseUrl});

  Future<List<Department>> fetchDepartments() async {
    final response = await http.get(Uri.parse('$baseUrl/departments'));
    if (response.statusCode == 200) {
      List<dynamic> jsonResponse = jsonDecode(response.body);
      return jsonResponse.map((department) => Department.fromJson(department)).toList();
    } else {
      throw Exception('Failed to load departments');
    }
  }
}