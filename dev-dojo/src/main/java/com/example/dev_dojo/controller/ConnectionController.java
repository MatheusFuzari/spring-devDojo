package com.example.dev_dojo.controller;


import com.example.dev_dojo.config.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/connections")
@Slf4j
@RequiredArgsConstructor
public class ConnectionController {

    private final Connection connection;

    @GetMapping()
    public ResponseEntity<Connection> getConnections() {
        return ResponseEntity.status(HttpStatus.OK).body(connection);

        /*
        * {
        *   "timestamp": "2025-02-03T14:29:11.769+00:00",
        *   "status": 406,
        *   "error": "Not Acceptable",
        *   "message": "Acceptable representations: [application/json, application/*+json].",
        *   "path": "/v1/connections"
        * }
        *
        * Usually this occurs when the external class doesn't have getter methods.
        * */

    }
}
