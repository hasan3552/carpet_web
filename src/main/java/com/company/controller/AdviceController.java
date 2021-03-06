package com.company.controller;

import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.exp.NoPermissionException;
import com.company.exp.NullFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {
    @ExceptionHandler({BadRequestException.class, ItemNotFoundException.class, NullFieldException.class})
    public ResponseEntity<String> handler(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler({NoPermissionException.class})
    public ResponseEntity<String> handler(NoPermissionException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

}
