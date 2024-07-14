import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'APIService.dart';
import 'EmployeeDTO.dart';

class AddEmployeeScreen extends StatefulWidget {
  @override
  _AddEmployeeScreenState createState() => _AddEmployeeScreenState();
}

class _AddEmployeeScreenState extends State<AddEmployeeScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final APIService _apiService = APIService(client: http.Client());

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      try {
        final newEmployee = EmployeeDTO(id: 0, name: _nameController.text);
        print('Creating employee: ${newEmployee.toJson()}'); // Debug message
        await _apiService.createEmployee(newEmployee);
        Navigator.pop(context, true);
      } catch (e) {
        print('Failed to create employee: $e'); // Debug message
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to create employee: $e')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Add New Employee')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: <Widget>[
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(labelText: 'Name'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Please enter a name';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: _submitForm,
                child: const Text('Add Employee'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    _nameController.dispose();
    super.dispose();
  }
}
