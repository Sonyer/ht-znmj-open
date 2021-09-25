package com.handturn.bole.common.function;

import com.handturn.bole.common.exception.RedisConnectException;

/**
 * @author Eric
 */
@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
