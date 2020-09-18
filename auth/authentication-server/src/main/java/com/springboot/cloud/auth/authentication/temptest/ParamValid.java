package com.springboot.cloud.auth.authentication.temptest;

import java.util.function.Function;

/**
 * 参数校验类
 * @param <T>
 */
public class ParamValid<T> {
    private T t;

    public ParamValid() {
    }

    public ParamValid(T t) {
        this.t = t;
    }

    public static <T> ParamValid<T> init(T t) {
        ParamValid<T> paramValid = new ParamValid<T>(t);
        return paramValid;
    }

    public Object end(Function<T, Object> function) {
        return function.apply(t);
    }

    /**
     * 校验（true的话校验不通过）
     *
     * @return ParamValid
     */
    public ParamValid<T> valid(Function<T, Boolean> function) {
        if (function.apply(t)) {
            throw new IllegalArgumentException("参数错误");
        }        return this;
    }
    /**
     * 校验（true的话校验不通过,错误信息）
     *
     * @return ParamValid
     */
    public ParamValid<T> valid(Function<T, Boolean> function, String message) {
        if (function.apply(t)) {
            throw new IllegalArgumentException(message);
        }        return this;
    }    /**
     * 校验（true的话校验不通过,错误信息）
     *
     * @return ParamValid
     */
    public ParamValid<T> valid(boolean condition, Function<T, Boolean> function, String message) {
        if (!condition) {
            return this;
        }        if (function.apply(t)) {
            throw new IllegalArgumentException(message);
        }        return this;
    }
}
