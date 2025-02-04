package com.example.dev_dojo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Data
public class Connection {
    private String host;
    private String username;
    private String password;
}
