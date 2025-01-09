package com.yash.linkedin.connection_service.controller;

import com.yash.linkedin.connection_service.dto.PersonDTO;
import com.yash.linkedin.connection_service.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path ="/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionService connectionService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<PersonDTO>> getFirstConnections() {
        return ResponseEntity.ok(connectionService.getFirstDegreeConnection());
    }
}
