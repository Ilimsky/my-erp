import 'dart:convert';
import 'package:http/http.dart' as http;

class APIServiceEmployee {
  final String baseUrl;

  APIServiceEmployee({required this.baseUrl});

  Future<List<dynamic>> fetchEmployees() async {
    final response = await http.get(Uri.parse('$baseUrl/employees'));
    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Failed to load employees');
    }
  }



// Дополнительные методы для создания, обновления и удаления сотрудников и отделов можно добавить здесь.
}