class Employee {
  final int id;
  final String name;
  final int departmentId;

  Employee({required this.id, required this.name, required this.departmentId});

  factory Employee.fromJson(Map<String, dynamic> json) {
    return Employee(
      id: json['id'],
      name: json['name'] ?? 'Unknown',
      departmentId: json['departmentId'] ?? 0,
    );
  }
}