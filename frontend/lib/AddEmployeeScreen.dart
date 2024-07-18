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
  final TextEditingController _nameController = TextEditingController();

  Future<void> _addEmployee() async {
    if (_formKey.currentState?.validate() ?? false) {
      final name = _nameController.text;
      final employeeDTO = EmployeeDTO(name: name);

      try {
        await APIService(client: http.Client()).createEmployee(employeeDTO);
        Navigator.pop(context, true); // Return true to indicate successful addition
      } catch (e) {
        // Handle any errors here, e.g., show a dialog
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to add employee: $e')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Add Employee'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
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
              const SizedBox(height: 16.0),
              ElevatedButton(
                onPressed: _addEmployee,
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
