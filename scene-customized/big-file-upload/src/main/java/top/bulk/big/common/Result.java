package top.bulk.big.common;


import java.io.Serializable;

/**
 * 通用返回结果
 *
 * @author 散装java
 * @date 2022-06-29
 */
public class Result<T> implements Serializable {
    /**
     * 错误信息，提供给调用者看
     */
    private String msg = "success";
    /**
     * 状态码，暂定 0 1， 后面可根据模块优化
     */
    private int code = 0;

    private boolean success = true;
    /**
     * 反参数据体
     */
    private T data;

    public Result() {
        super();
    }

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, T data) {
        this(code);
        this.data = data;
    }

    public Result(int code, String msg) {
        this(code);
        this.msg = msg;
    }

    public Result(int code, boolean success, String msg) {
        this(code);
        this.msg = msg;
        this.success = success;
    }

    public Result(int code, T data, String msg) {
        this(code, msg);
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Result<T> message(String msg) {
        this.msg = msg;
        return this;
    }
}
