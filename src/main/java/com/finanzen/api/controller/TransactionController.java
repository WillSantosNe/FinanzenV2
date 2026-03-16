package com.finanzen.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finanzen.api.dto.TransactionCreateDto;
import com.finanzen.api.dto.TransactionGetDto;
import com.finanzen.api.dto.TransactionUpdateDto;
import com.finanzen.api.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/transactions")
public class TransactionController {

    // Realizando injeção de dependencias do Service
    private final TransactionService service;

    public TransactionController(TransactionService service){
        this.service = service;
    }

    @PostMapping                        //Devemos usar @Valid para o Spring fazer as validações necessárias
    public ResponseEntity<TransactionGetDto> createTransaction(@RequestBody @Valid TransactionCreateDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTransaction(dto)); 
    }

    @GetMapping
    public ResponseEntity<Page<TransactionGetDto>> findAll(@PageableDefault(size = 10, sort = {"createdAt"}) Pageable pagination) {
        return ResponseEntity.ok(service.findAll(pagination));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionGetDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TransactionGetDto> update(@PathVariable Long id, @RequestBody @Valid TransactionUpdateDto dto){
        return ResponseEntity.ok(service.update(id, dto));
    }
}
