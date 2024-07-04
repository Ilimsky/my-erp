import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../models/employee.dart';
import '../providers/employee_provider.dart';
// import 'package:employee_app/providers/employee_provider.dart';
// import 'package:employee_app/models/employee.dart';

class EmployeeScreen extends StatefulWidget {
  @override
  _EmployeeScreenState createState() => _EmployeeScreenState();
}

class _EmployeeScreenState extends State<EmployeeScreen> {
  final _nameController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Employee App'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _nameController,
              decoration: InputDecoration(labelText: 'Name'),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () async {
                final name = _nameController.text;
                if (name.isNotEmpty) {
                  final employee = Employee(name: name);
                  await Provider.of<EmployeeProvider>(context, listen: false)
                      .createEmployee(employee);
                }
              },
              child: Text('Create Employee'),
            ),
            SizedBox(height: 20),
            Consumer<EmployeeProvider>(
              builder: (context, provider, child) {
                return provider.employee != null
                    ? Text('Employee Created: ${provider.employee!.name}')
                    : Text('No employee created yet.');
              },
            ),
          ],
        ),
      ),
    );
  }
}
