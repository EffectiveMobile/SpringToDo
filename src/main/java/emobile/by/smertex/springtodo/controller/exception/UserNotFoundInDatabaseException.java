package emobile.by.smertex.springtodo.controller.exception;

import emobile.by.smertex.springtodo.util.ResponseMessage;
import lombok.Getter;

/**
 * Ошибка нахождения пользователя в БД при проведении различных операций на уровне сервисов
 */
@Getter
public class UserNotFoundInDatabaseException extends RuntimeException{

    private final String message;

    public UserNotFoundInDatabaseException(Throwable e, String user){
        super(e);
        this.message = ResponseMessage.USER_NOT_FOUND_EXCEPTION + " " + user;
    }

    public UserNotFoundInDatabaseException(String user){
        super(new RuntimeException());
        this.message = ResponseMessage.USER_NOT_FOUND_EXCEPTION + " " + user;
    }
}
