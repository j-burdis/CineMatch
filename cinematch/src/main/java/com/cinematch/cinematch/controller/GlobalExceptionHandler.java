package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.exception.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMovieNotFound(MovieNotFoundException e, Model model) {
        System.out.println("in handleMovieNotFound");
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRunTimeException(RuntimeException e,  Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/500";
    }

}



