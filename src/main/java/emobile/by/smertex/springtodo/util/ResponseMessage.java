package emobile.by.smertex.springtodo.util;

/**
 * Основной набор сообщений, которые отсылаются для передачи информации пользователю в случае,
 * когда нет конкретного возвращаемого JSON
 */
public final class ResponseMessage {
    public static final String USER_NOT_FOUND_EXCEPTION = "User not found";
    public static final String UNAUTHORIZED_USER = "Incorrect login or password";
    public static final String INVALID_JWT = "Invalid jwt";
    public static final String UPDATE_TASK_NOT_FOUND = "The task to be updated was not found";
    public static final String CREATE_TASK_EXCEPTION = "Incorrect data to save";
    public static final String ADD_COMMENT_EXCEPTION = "When trying to add a comment to a task, incorrect data was entered";
    public static final String UPDATE_COMMENT_EXCEPTION = "You don't have permission";
    public static final String DELETE_TASK_SUCCESSFULLY = "Task deleted";
    public static final String DELETE_TASK_FAILED = "The task was not deleted";
    public static final String SAVE_FAILED_DUE_DUPLICATE = "A data with the same name already exists";
    public static final String SAVE_METAINFO_FAILED = "Save metainfo failed";
}
