class EmployeeDTO {
  int id;
  String name;

  EmployeeDTO({required this.id, required this.name});

  factory EmployeeDTO.fromJson(Map<String, dynamic> json) {
    return EmployeeDTO(
      id: json['id'] as int? ?? 0,
      name: json['name'] as String? ?? 'Unknown',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
    };
  }
}