import 'dart:convert';
import 'package:http/http.dart' as http;

class APIServiceDepartment {

  final String baseUrl;

  APIServiceDepartment({required this.baseUrl});

    Future<List<dynamic>> fetchDepartments() async {
    final response = await http.get(Uri.parse('$baseUrl/departments'));
    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Failed to load departments');
    }
  }

// Дополнительные методы для создания, обновления и удаления сотрудников и отделов можно добавить здесь.
}