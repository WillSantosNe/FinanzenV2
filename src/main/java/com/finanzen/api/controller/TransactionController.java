package com.finanzen.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finanzen.api.dto.TransactionCreateDto;
import com.finanzen.api.dto.TransactionGetDto;
import com.finanzen.api.service.TransactionService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/transactions")
public class TransactionController {

    // Realizando injeção de dependencias do Service
    private final TransactionService service;

    public TransactionController(TransactionService service){
        this.service = service;
    }

    @PostMapping
    public void createTransaction(@RequestBody TransactionCreateDto dto){
        service.createTransaction(dto);
    }

    @GetMapping
    public List<TransactionGetDto> findAll() {
        return service.findAll();
    }
    
}
