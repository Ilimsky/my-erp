package contracts

org.springframework.cloud.contract.spec.Contract.make {
    description "Get department by ID"

    request {
        method GET()
        url "/department/1"
    }

    response {
        status 200
        body([
                departmentDTOId: 1,
                departmentDTOName: "HR"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}