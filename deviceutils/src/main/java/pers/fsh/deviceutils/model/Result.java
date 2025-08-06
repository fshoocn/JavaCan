package pers.fsh.deviceutils.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * 统一返回结果类
 *
 * @author fanshuhua
 * @date 2025/5/19 19:18
 */
@Getter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final int code;
    private final String message;
    private final T data;

    private Result(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(true, 1, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, 1, "success", data);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(true, 1, message, null);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, 1, message, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(false, -1, "error", null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(false, -1, message, null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(false, code, message, null);
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(false, code, message, data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
