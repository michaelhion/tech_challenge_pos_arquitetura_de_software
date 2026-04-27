package com.techchallenger.oficina360.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @GetMapping("/ola")
    public ResponseEntity<String> olaMundo(){
        return ResponseEntity.ok("ola mundo!");
    }
}
