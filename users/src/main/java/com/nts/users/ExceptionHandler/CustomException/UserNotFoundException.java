package com.nts.users.ExceptionHandler.CustomException;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String msg){
        super(msg);
    }

    public UserNotFoundException(){
        super("User not found Exception: ");
    }
}
