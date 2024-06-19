package com.example.departmentservice.dtos;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO implements Serializable {
    private Long id;
    private String name;
    private String address;
}
