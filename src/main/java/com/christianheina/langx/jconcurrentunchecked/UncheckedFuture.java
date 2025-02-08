/*
 * Copyright 2024 Christian Heina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.christianheina.langx.jconcurrentunchecked;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import com.christianheina.langx.jconcurrentunchecked.exceptions.UncheckedConcurrentExecutionException;
import com.christianheina.langx.jconcurrentunchecked.exceptions.UncheckedConcurrentInterruptedException;
import com.christianheina.langx.jconcurrentunchecked.exceptions.UncheckedConcurrentTimeoutException;

/**
 * Unchecked future implementation.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class UncheckedFuture<T> implements Future<T> {

    private Future<T> future;

    public UncheckedFuture(Future<T> future) {
        this.future = future;
    }

    /**
     * Returns a new UncheckedFuture that is already completed with the given value.
     *
     * @param value
     *            the value
     * @param <U>
     *            the type of the value
     * 
     * @return the completed UncheckedFuture
     */
    public static <U> UncheckedFuture<U> completedUncheckedFuture(U value) {
        return UncheckedCompletableFuture.completedUncheckedCompletableFuture(value);
    }

    /**
     * Returns a new UncheckedFuture that is asynchronously completed by a task running in the
     * {@link ForkJoinPool#commonPool()} after it runs the given action.
     *
     * @param runnable
     *            the action to run before completing the returned UncheckedFuture
     * 
     * @return the new UncheckedFuture
     */
    public static UncheckedFuture<Void> runAsync(Runnable runnable) {
        return UncheckedCompletableFuture.runAsync(runnable);
    }

    /**
     * Returns a new UncheckedFuture that is asynchronously completed by a task running in the given executor after it
     * runs the given action.
     *
     * @param runnable
     *            the action to run before completing the returned UncheckedFuture
     * @param executor
     *            the executor to use for asynchronous execution
     * 
     * @return the new UncheckedFuture
     */
    public static UncheckedFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return UncheckedCompletableFuture.runAsync(runnable, executor);
    }

    /**
     * Returns a new UncheckedFuture that is asynchronously completed by a task running in the
     * {@link ForkJoinPool#commonPool()} with the value obtained by calling the given Supplier.
     *
     * @param supplier
     *            a function returning the value to be used to complete the returned UncheckedFuture
     * @param <U>
     *            the function's return type
     * 
     * @return the new UncheckedFuture
     */
    public static <U> UncheckedFuture<U> supplyAsync(Supplier<U> supplier) {
        return UncheckedCompletableFuture.supplyAsync(supplier);
    }

    /**
     * Returns a new UncheckedFuture that is asynchronously completed by a task running in the given executor with the
     * value obtained by calling the given Supplier.
     *
     * @param supplier
     *            a function returning the value to be used to complete the returned UncheckedFuture
     * @param executor
     *            the executor to use for asynchronous execution
     * @param <U>
     *            the function's return type
     * 
     * @return the new UncheckedFuture
     */
    public static <U> UncheckedFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return UncheckedCompletableFuture.supplyAsync(supplier, executor);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public T get() {
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new UncheckedConcurrentInterruptedException(e);
        } catch (ExecutionException e) {
            throw new UncheckedConcurrentExecutionException(e);
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) {
        try {
            return future.get(timeout, unit);
        } catch (InterruptedException e) {
            throw new UncheckedConcurrentInterruptedException(e);
        } catch (ExecutionException e) {
            throw new UncheckedConcurrentExecutionException(e);
        } catch (TimeoutException e) {
            throw new UncheckedConcurrentTimeoutException(e);
        }
    }

}
