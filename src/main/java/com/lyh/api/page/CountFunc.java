package com.lyh.api.page;

/**
 * @author shoushen.luan
 * @since 2022-10-23
 */
@FunctionalInterface
public interface CountFunc<T> {
    int count();
}