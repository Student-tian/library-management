package com.henu.controller.request;

import lombok.Data;

@Data
public class AdminPageRequest extends BaseRequest{
    private String username;
    private String email;
    private String phone;
}
