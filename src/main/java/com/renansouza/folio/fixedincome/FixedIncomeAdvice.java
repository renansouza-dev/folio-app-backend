package com.renansouza.folio.fixedincome;

import com.renansouza.folio.fixedincome.exception.FixedIncomeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class FixedIncomeAdvice {
    @ResponseBody
    @ExceptionHandler(FixedIncomeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String fixedIncomeNotFoundHandler(FixedIncomeNotFoundException ex) {
        return ex.getMessage();
    }

}