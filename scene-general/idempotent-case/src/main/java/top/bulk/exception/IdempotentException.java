package top.bulk.exception;

/**
 * 不满足幂等要求后抛出的注解
 * 方便后期做全局异常处理
 *
 * @author 散装java
 * @date 2024-06-13
 */
public class IdempotentException extends RuntimeException {
    public IdempotentException() {
        super();
    }

    public IdempotentException(String message) {
        super(message);
    }

    public IdempotentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdempotentException(Throwable cause) {
        super(cause);
    }

    protected IdempotentException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
