package top.bulk.big.common;

/**
 * 生成返回结果的工具类
 *
 * @author 散装java
 * @date 2022-06-29
 */
public class Res  {

    private final static String SUCCESS = "success";
    private final static int SUCCESS_CODE = 0;
    private final static int FAIL_CODE = 1;

    private final static String FAILED = "failed";

    public static <T> Result<T> ok() {
        return new Result<T>(SUCCESS_CODE, SUCCESS);
    }

    public static <T> Result<T> ok(String message) {
        return new Result<T>(SUCCESS_CODE, message);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<T>(SUCCESS_CODE, data, SUCCESS);
    }

    public static <T> Result<T> error() {
        return new Result<T>(FAIL_CODE, false, FAILED);
    }

    public static <T> Result<T> error(String message) {
        return new Result<T>(FAIL_CODE, false, message);
    }

    public static <T> Result<T> response(int code, String msg) {
        return new Result<T>(code, false, msg);
    }

    public static <T> Result<T> response(int code, String msg, T data) {
        return new Result<T>(code, data, msg);
    }
}
