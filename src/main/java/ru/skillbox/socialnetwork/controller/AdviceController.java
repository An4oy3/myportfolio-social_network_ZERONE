package ru.skillbox.socialnetwork.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.skillbox.socialnetwork.data.dto.ErrorResponse;
import ru.skillbox.socialnetwork.exception.DialogNotFoundException;
import ru.skillbox.socialnetwork.exception.MessageNotFoundException;
import ru.skillbox.socialnetwork.exception.PersonNotAuthorized;
import ru.skillbox.socialnetwork.exception.PostNotFoundException;

public interface AdviceController {

    ResponseEntity<ErrorResponse> userNotFoundExceptionHandler(UsernameNotFoundException exception);

    ResponseEntity<ErrorResponse> personNotAuthorizedExceptionHandler(PersonNotAuthorized exception);

    ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception);

    ResponseEntity<ErrorResponse> postNotFoundExceptionHandler(PostNotFoundException exception);

    ResponseEntity<ErrorResponse> dialogNotFoundExceptionHandler(DialogNotFoundException exception);

    ResponseEntity<ErrorResponse> messageNotFoundExceptionHandler(MessageNotFoundException exception);

    ResponseEntity<ErrorResponse> noHandlerFoundExceptionHandler(NoHandlerFoundException exception);
}
