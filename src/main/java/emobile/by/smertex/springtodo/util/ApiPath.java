package emobile.by.smertex.springtodo.util;

/**
 * Эндпоинты данного API по которым можно получить доступ
 */
public final class ApiPath {
    public static final String API_PATH = "/api";
    public static final String COMMENT_PATH = "/comment";
    public static final String TASK_PATH = API_PATH + "/task";
    public static final String ID_PATH = "/{id}";
    public static final String ALL_PATH = "/**";
    public static final String AUTH_PATH = API_PATH + "/auth";
    public static final String SWAGGER_PATH = "/swagger-ui" + ALL_PATH;
    public static final String SWAGGER_DOCS_PATH = "/v3/api-docs" + ALL_PATH;
    public static final String COMMENT_IN_TASK_PATH = ID_PATH + COMMENT_PATH;
    public static final String COMMENT_UPDATE_PATH = COMMENT_PATH + ID_PATH;
    public static final String ID_TASK_PATH = ID_PATH;
}
