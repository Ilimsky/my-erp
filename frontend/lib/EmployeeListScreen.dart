import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'APIService.dart';
import 'Employee.dart';
import 'AddEmployeeScreen.dart';

class EmployeeListScreen extends StatefulWidget {
  @override
  _EmployeeListScreenState createState() => _EmployeeListScreenState();
}

class _EmployeeListScreenState extends State<EmployeeListScreen> {
  late Future<List<Employee>> _employeesFuture;

  @override
  void initState() {
    super.initState();
    _employeesFuture = APIService(client: http.Client()).getAllEmployees();
  }

  Future<void> _refreshEmployees() async {
    setState(() {
      _employeesFuture = APIService(client: http.Client()).getAllEmployees();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Список сотрудников'),
      ),
      body: FutureBuilder<List<Employee>>(
        future: _employeesFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data == null) {
            return const Center(child: Text('No data available'));
          } else {
            final employees = snapshot.data!;
            return RefreshIndicator(
              onRefresh: _refreshEmployees,
              child: ListView.builder(
                itemCount: employees.length,
                itemBuilder: (context, index) {
                  final employee = employees[index];
                  return ListTile(
                    title: Text('ID: ${employee.id}'),
                    subtitle: Text(employee.name),
                  );
                },
              ),
            );
          }
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          final result = await Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => AddEmployeeScreen()),
          );
          if (result == true) {
            _refreshEmployees();
          }
        },
        child: Icon(Icons.add),
        tooltip: 'Добавить нового сотрудника',
      ),
    );
  }
}
