package emobile.by.smertex.springtodo.controller.exception;

import lombok.Getter;

/**
 * Ошибка сохранения метаинфорамации задачи в БД
 */
@Getter
public class SaveMetainfoException extends RuntimeException{

    private final String message;

    public SaveMetainfoException(String message){
        super(new RuntimeException());
        this.message = message;
    }

}
