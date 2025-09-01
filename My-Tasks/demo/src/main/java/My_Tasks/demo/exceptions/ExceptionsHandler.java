package My_Tasks.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler{

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMsg> notFoundException(NotFoundException exception){
        ErrorMsg erro = new ErrorMsg(HttpStatus.NOT_FOUND.value(),exception.getMessage());
        return new ResponseEntity<>(erro,HttpStatus.NOT_FOUND);
    }
}
