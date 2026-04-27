package com.techchallenger.oficina360.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
class ClientesControllerTest {

    @InjectMocks
    private ClientesController clientesController;

    @Test
    void shouldReturnOla(){
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("ola mundo!");
        assertEquals(expectedResponse,clientesController.olaMundo());
    }
}