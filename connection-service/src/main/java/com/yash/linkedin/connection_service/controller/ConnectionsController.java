package com.yash.linkedin.connection_service.controller;

import com.yash.linkedin.connection_service.dto.PersonDTO;
import com.yash.linkedin.connection_service.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/request/{userId}")
    public ResponseEntity<Void> requestConnection(@PathVariable Long userId){
        connectionService.requestConnection(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/accept/{userId}")
    public ResponseEntity<Void> acceptConnectionRequest(@PathVariable Long userId){
        connectionService.acceptConnectionRequest(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/reject/{userId}")
    public ResponseEntity<Void> rejectConnectionRequest(@PathVariable Long userId){
        connectionService.rejectConnectionRequest(userId);
        return ResponseEntity.ok().build();
    }
}
