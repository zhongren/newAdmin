package com.admin.model.req;

import org.hibernate.validator.constraints.NotBlank;

public class TestRequest {
    @NotBlank(message="不为空")
    private String name;

}
