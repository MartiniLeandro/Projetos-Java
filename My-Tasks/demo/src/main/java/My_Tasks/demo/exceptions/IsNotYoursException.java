package My_Tasks.demo.exceptions;

public class IsNotYoursException extends RuntimeException{
    public IsNotYoursException(String msg){
        super(msg);
    }
}
