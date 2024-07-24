class Employee {
  final int id;
  final String name;
  final int departmentId;

  Employee({required this.id, required this.name, required this.departmentId});

  factory Employee.fromJson(Map<String, dynamic> json) {
    return Employee(
      id: json['employeeDTOId'] ?? 0,
      name: json['employeeDTOName'] ?? 'Unknown',
      departmentId: json['departmentId'] ?? 0,
    );
  }
}
