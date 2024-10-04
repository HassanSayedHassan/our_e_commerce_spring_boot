package com.example.our_e_commerce.responce;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private String message;

    private Object data;
}
