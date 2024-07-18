import 'package:json_annotation/json_annotation.dart';

part 'EmployeeDTO.g.dart';

@JsonSerializable()
class EmployeeDTO {
  final int? id;
  final String name;

  EmployeeDTO({this.id, required this.name});

  factory EmployeeDTO.fromJson(Map<String, dynamic> json) => _$EmployeeDTOFromJson(json);
  Map<String, dynamic> toJson() => _$EmployeeDTOToJson(this);
}















// class EmployeeDTO {
//   int id;
//   String name;
//
//   EmployeeDTO({required this.id, required this.name});
//
//   factory EmployeeDTO.fromJson(Map<String, dynamic> json) {
//     return EmployeeDTO(
//       id: json['id'] as int? ?? 0,
//       name: json['name'] as String? ?? 'Unknown',
//     );
//   }
//
//   Map<String, dynamic> toJson() {
//     return {
//       'id': id,
//       'name': name,
//     };
//   }
// }