package pers.fsh.deviceutils.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fanshuhua
 * @date 2025/5/19 19:18
 */
@Setter
@Getter
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result() {
    }

    public static Result<String> success() {
        return new Result<>(1, "success");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(1, "success", data);
    }

    public static Result<String> success(String msg) {
        return new Result<>(1, msg);
    }

    public static Result<String> success(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(1, msg, data);
    }

    public static <T> Result<T> success(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }


    public static Result<String> error() {
        return new Result<>(-1, "error");
    }

    public static Result<String> error(String msg) {
        return new Result<>(-1, msg);
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(-1, "error", data);
    }

    public static Result<String> error(int code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> error(String msg, T data) {
        return new Result<>(-1, msg, data);
    }

    public static <T> Result<T> error(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
