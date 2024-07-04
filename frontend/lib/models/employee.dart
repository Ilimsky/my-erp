class Employee {
  final int? id;
  final String name;

  Employee({this.id, required this.name});

  factory Employee.fromJson(Map<String, dynamic> json) {
    return Employee(
      id: json['employeeDTOId'],
      name: json['employeeDTOName']
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'employeeDTOId': id,
      'employeeDTOName': name,
    };
  }
}
