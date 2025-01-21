package com.example.dev_dojo.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AnimePutRequest {
    private Long id;
    private String anime;
}
