package com.finanzen.api.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Vai capturar o EntityNotFoundException lançado e vai retornar o erro 404.
    // Isso permite uma leitura mais precisa do motivo da requisição ter falhado.
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFound(EntityNotFoundException e){
        return ResponseEntity.notFound().build();
    }

    // Record que vai trazer o campo e a mensagem de erro do campo.
    // Estou personalizando um retorno para a requisição, para evitar envio de campos desnecessários.
    public record FieldErroDto(String field, String message) {
        public FieldErroDto(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldErroDto>> handleBadRequest(MethodArgumentNotValidException e){
        var errors = e.getFieldErrors();

        List<FieldErroDto> listErrors = errors.stream().map(error -> new FieldErroDto(error)).toList();

        return ResponseEntity.badRequest().body(listErrors);

    }

}
