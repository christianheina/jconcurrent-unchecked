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

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * UncheckedCompletableFuture implementation.z<br>
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class UncheckedCompletableFuture<T> extends UncheckedFuture<T> implements CompletionStage<T> {

    private CompletableFuture<T> future;

    public UncheckedCompletableFuture(CompletableFuture<T> future) {
        super(future);
        this.future = future;
    }

    /**
     * Returns a new UncheckedCompletableFuture that is completed when all of the given UncheckedCompletableFutures
     * complete. If any of the given UncheckedCompletableFutures complete exceptionally, then the returned
     * UncheckedCompletableFuture also does so, with a CompletionException holding this exception as its cause.
     * Otherwise, the results, if any, of the given UncheckedCompletableFutures are not reflected in the returned
     * UncheckedCompletableFuture, but may be obtained by inspecting them individually. If no
     * UncheckedCompletableFutures are provided, returns a UncheckedCompletableFuture completed with the value
     * {@code null}.
     *
     * <p>
     * Among the applications of this method is to await completion of a set of independent UncheckedCompletableFutures
     * before continuing a program, as in: {@code UncheckedCompletableFuture.allOf(c1, c2, c3).join();}.
     *
     * @param UncheckedCompletableFutures
     *            the UncheckedCompletableFutures
     * 
     * @return a new UncheckedCompletableFuture that is completed when all of the given UncheckedCompletableFutures
     *         complete
     * 
     * @throws NullPointerException
     *             if the array or any of its elements are {@code null}
     */
    public static UncheckedCompletableFuture<Void> allOf(UncheckedCompletableFuture<?>... UncheckedCompletableFutures) {
        return new UncheckedCompletableFuture<Void>(
                CompletableFuture.allOf(uncheckedCompletableFuturesToCompletableFutures(UncheckedCompletableFutures)));
    }

    /**
     * Returns a new UncheckedCompletableFuture that is completed when any of the given UncheckedCompletableFutures
     * complete, with the same result. Otherwise, if it completed exceptionally, the returned UncheckedCompletableFuture
     * also does so, with a CompletionException holding this exception as its cause. If no UncheckedCompletableFutures
     * are provided, returns an incomplete UncheckedCompletableFuture.
     *
     * @param UncheckedCompletableFutures
     *            the UncheckedCompletableFutures
     * 
     * @return a new UncheckedCompletableFuture that is completed with the result or exception of any of the given
     *         UncheckedCompletableFutures when one completes
     * 
     * @throws NullPointerException
     *             if the array or any of its elements are {@code null}
     */
    public static UncheckedCompletableFuture<Object> anyOf(
            UncheckedCompletableFuture<?>... UncheckedCompletableFutures) {
        return new UncheckedCompletableFuture<Object>(
                CompletableFuture.anyOf(uncheckedCompletableFuturesToCompletableFutures(UncheckedCompletableFutures)));
    }

    /**
     * Returns a new UncheckedCompletableFuture that is already completed with the given value.
     *
     * @param value
     *            the value
     * @param <U>
     *            the type of the value
     * 
     * @return the completed UncheckedCompletableFuture
     */
    public static <U> UncheckedCompletableFuture<U> completedUncheckedCompletableFuture(U value) {
        return new UncheckedCompletableFuture<U>(CompletableFuture.completedFuture(value));
    }

    /**
     * Returns a new UncheckedCompletableFuture that is asynchronously completed by a task running in the
     * {@link ForkJoinPool#commonPool()} after it runs the given action.
     *
     * @param runnable
     *            the action to run before completing the returned UncheckedCompletableFuture
     * 
     * @return the new UncheckedCompletableFuture
     */
    public static UncheckedCompletableFuture<Void> runAsync(Runnable runnable) {
        return new UncheckedCompletableFuture<Void>(CompletableFuture.runAsync(runnable));
    }

    /**
     * Returns a new UncheckedCompletableFuture that is asynchronously completed by a task running in the given executor
     * after it runs the given action.
     *
     * @param runnable
     *            the action to run before completing the returned UncheckedCompletableFuture
     * @param executor
     *            the executor to use for asynchronous execution
     * 
     * @return the new UncheckedCompletableFuture
     */
    public static UncheckedCompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return new UncheckedCompletableFuture<Void>(CompletableFuture.runAsync(runnable, executor));
    }

    /**
     * Returns a new UncheckedCompletableFuture that is asynchronously completed by a task running in the
     * {@link ForkJoinPool#commonPool()} with the value obtained by calling the given Supplier.
     *
     * @param supplier
     *            a function returning the value to be used to complete the returned UncheckedCompletableFuture
     * @param <U>
     *            the function's return type
     * 
     * @return the new UncheckedCompletableFuture
     */
    public static <U> UncheckedCompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return new UncheckedCompletableFuture<U>(CompletableFuture.supplyAsync(supplier));
    }

    /**
     * Returns a new UncheckedCompletableFuture that is asynchronously completed by a task running in the given executor
     * with the value obtained by calling the given Supplier.
     *
     * @param supplier
     *            a function returning the value to be used to complete the returned UncheckedCompletableFuture
     * @param executor
     *            the executor to use for asynchronous execution
     * @param <U>
     *            the function's return type
     * 
     * @return the new UncheckedCompletableFuture
     */
    public static <U> UncheckedCompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return new UncheckedCompletableFuture<U>(CompletableFuture.supplyAsync(supplier, executor));
    }

    private static CompletableFuture<?>[] uncheckedCompletableFuturesToCompletableFutures(
            UncheckedCompletableFuture<?>... UncheckedCompletableFutures) {
        CompletableFuture<?>[] cfs = new CompletableFuture<?>[UncheckedCompletableFutures.length];
        for (int i = 0; i < UncheckedCompletableFutures.length; i++) {
            cfs[i] = UncheckedCompletableFutures[i].future;
        }
        return cfs;
    }

    /**
     * Returns the result value when complete, or throws an (unchecked) exception if completed exceptionally. To better
     * conform with the use of common functional forms, if a computation involved in the completion of this
     * UncheckedCompletableFuture threw an exception, this method throws an (unchecked) {@link CompletionException} with
     * the underlying exception as its cause.
     *
     * @return the result value
     * 
     * @throws CancellationException
     *             if the computation was cancelled
     * @throws CompletionException
     *             if this future completed exceptionally or a completion computation threw an exception
     */
    public T join() {
        return future.join();
    }

    /**
     * Returns the result value (or throws any encountered exception) if completed, else returns the given
     * valueIfAbsent.
     *
     * @param valueIfAbsent
     *            the value to return if not completed
     * 
     * @return the result value, if completed, else the given valueIfAbsent
     * 
     * @throws CancellationException
     *             if the computation was cancelled
     * @throws CompletionException
     *             if this future completed exceptionally or a completion computation threw an exception
     */
    public T getNow(T valueIfAbsent) {
        return future.getNow(valueIfAbsent);
    }

    /**
     * If not already completed, sets the value returned by {@link #get()} and related methods to the given value.
     *
     * @param value
     *            the result value
     * 
     * @return {@code true} if this invocation caused this UncheckedCompletableFuture to transition to a completed
     *         state, else {@code false}
     */
    public boolean complete(T value) {
        return future.complete(value);
    }

    /**
     * If not already completed, causes invocations of {@link #get()} and related methods to throw the given exception.
     *
     * @param ex
     *            the exception
     * 
     * @return {@code true} if this invocation caused this UncheckedCompletableFuture to transition to a completed
     *         state, else {@code false}
     */
    public boolean completeExceptionally(Throwable ex) {
        return future.completeExceptionally(ex);
    }

    /**
     * Returns {@code true} if this UncheckedCompletableFuture completed exceptionally, in any way. Possible causes
     * include cancellation, explicit invocation of {@code completeExceptionally}, and abrupt termination of a
     * CompletionStage action.
     *
     * @return {@code true} if this UncheckedCompletableFuture completed exceptionally
     */
    public boolean isCompletedExceptionally() {
        return future.isCompletedExceptionally();
    }

    /**
     * Forcibly sets or resets the value subsequently returned by method {@link #get()} and related methods, whether or
     * not already completed. This method is designed for use only in error recovery actions, and even in such
     * situations may result in ongoing dependent completions using established versus overwritten outcomes.
     *
     * @param value
     *            the completion value
     */
    public void obtrudeValue(T value) {
        future.obtrudeValue(value);
    }

    /**
     * Forcibly causes subsequent invocations of method {@link #get()} and related methods to throw the given exception,
     * whether or not already completed. This method is designed for use only in error recovery actions, and even in
     * such situations may result in ongoing dependent completions using established versus overwritten outcomes.
     *
     * @param ex
     *            the exception
     * 
     * @throws NullPointerException
     *             if the exception is null
     */
    public void obtrudeException(Throwable ex) {
        future.obtrudeException(ex);
    }

    /**
     * Returns the estimated number of UncheckedCompletableFutures whose completions are awaiting completion of this
     * UncheckedCompletableFuture. This method is designed for use in monitoring system state, not for synchronization
     * control.
     *
     * @return the number of dependent UncheckedCompletableFutures
     */
    public int getNumberOfDependents() {
        return future.getNumberOfDependents();
    }

    @Override
    public <U> UncheckedCompletableFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        return new UncheckedCompletableFuture<U>(future.thenApply(fn));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        return new UncheckedCompletableFuture<U>(future.thenApplyAsync(fn));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn, Executor executor) {
        return new UncheckedCompletableFuture<U>(future.thenApplyAsync(fn, executor));
    }

    @Override
    public UncheckedCompletableFuture<Void> thenAccept(Consumer<? super T> action) {
        return new UncheckedCompletableFuture<Void>(future.thenAccept(action));
    }

    @Override
    public UncheckedCompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action) {
        return new UncheckedCompletableFuture<Void>(future.thenAcceptAsync(action));
    }

    @Override
    public UncheckedCompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor) {
        return new UncheckedCompletableFuture<Void>(future.thenAcceptAsync(action, executor));
    }

    @Override
    public UncheckedCompletableFuture<Void> thenRun(Runnable action) {
        return new UncheckedCompletableFuture<Void>(future.thenRun(action));
    }

    @Override
    public UncheckedCompletableFuture<Void> thenRunAsync(Runnable action) {
        return new UncheckedCompletableFuture<Void>(future.thenRunAsync(action));
    }

    @Override
    public UncheckedCompletableFuture<Void> thenRunAsync(Runnable action, Executor executor) {
        return new UncheckedCompletableFuture<Void>(future.thenRunAsync(action, executor));
    }

    @Override
    public <U, V> UncheckedCompletableFuture<V> thenCombine(CompletionStage<? extends U> other,
            BiFunction<? super T, ? super U, ? extends V> fn) {
        return new UncheckedCompletableFuture<V>(future.thenCombine(other, fn));
    }

    @Override
    public <U, V> UncheckedCompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other,
            BiFunction<? super T, ? super U, ? extends V> fn) {
        return new UncheckedCompletableFuture<V>(future.thenCombineAsync(other, fn));
    }

    @Override
    public <U, V> UncheckedCompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other,
            BiFunction<? super T, ? super U, ? extends V> fn, Executor executor) {
        return new UncheckedCompletableFuture<V>(future.thenCombineAsync(other, fn, executor));
    }

    @Override
    public <U> UncheckedCompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> other,
            BiConsumer<? super T, ? super U> action) {
        return new UncheckedCompletableFuture<Void>(future.thenAcceptBoth(other, action));
    }

    @Override
    public <U> UncheckedCompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,
            BiConsumer<? super T, ? super U> action) {
        return new UncheckedCompletableFuture<Void>(future.thenAcceptBothAsync(other, action));
    }

    @Override
    public <U> UncheckedCompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,
            BiConsumer<? super T, ? super U> action, Executor executor) {
        return new UncheckedCompletableFuture<Void>(future.thenAcceptBothAsync(other, action, executor));
    }

    @Override
    public UncheckedCompletableFuture<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
        return new UncheckedCompletableFuture<Void>(future.runAfterBoth(other, action));
    }

    @Override
    public UncheckedCompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        return new UncheckedCompletableFuture<Void>(future.runAfterBothAsync(other, action));
    }

    @Override
    public UncheckedCompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action,
            Executor executor) {
        return new UncheckedCompletableFuture<Void>(future.runAfterEitherAsync(other, action, executor));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> applyToEither(CompletionStage<? extends T> other,
            Function<? super T, U> fn) {
        return new UncheckedCompletableFuture<U>(future.applyToEither(other, fn));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other,
            Function<? super T, U> fn) {
        return new UncheckedCompletableFuture<U>(future.applyToEitherAsync(other, fn));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other,
            Function<? super T, U> fn, Executor executor) {
        return new UncheckedCompletableFuture<U>(future.applyToEitherAsync(other, fn, executor));
    }

    @Override
    public UncheckedCompletableFuture<Void> acceptEither(CompletionStage<? extends T> other,
            Consumer<? super T> action) {
        return new UncheckedCompletableFuture<Void>(future.acceptEither(other, action));
    }

    @Override
    public UncheckedCompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other,
            Consumer<? super T> action) {
        return new UncheckedCompletableFuture<Void>(future.acceptEitherAsync(other, action));
    }

    @Override
    public UncheckedCompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other,
            Consumer<? super T> action, Executor executor) {
        return new UncheckedCompletableFuture<Void>(future.acceptEitherAsync(other, action, executor));
    }

    @Override
    public UncheckedCompletableFuture<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
        return new UncheckedCompletableFuture<Void>(future.runAfterEither(other, action));
    }

    @Override
    public UncheckedCompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        return new UncheckedCompletableFuture<Void>(future.runAfterBothAsync(other, action));
    }

    @Override
    public UncheckedCompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action,
            Executor executor) {
        return new UncheckedCompletableFuture<Void>(future.runAfterEitherAsync(other, action, executor));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn) {
        return new UncheckedCompletableFuture<U>(future.thenCompose(fn));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
        return new UncheckedCompletableFuture<U>(future.thenComposeAsync(fn));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn,
            Executor executor) {
        return new UncheckedCompletableFuture<U>(future.thenComposeAsync(fn, executor));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        return new UncheckedCompletableFuture<U>(future.handle(fn));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
        return new UncheckedCompletableFuture<U>(future.handleAsync(fn));
    }

    @Override
    public <U> UncheckedCompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn,
            Executor executor) {
        return new UncheckedCompletableFuture<U>(future.handleAsync(fn, executor));
    }

    @Override
    public UncheckedCompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        return new UncheckedCompletableFuture<T>(future.whenComplete(action));
    }

    @Override
    public UncheckedCompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        return new UncheckedCompletableFuture<T>(future.whenCompleteAsync(action));
    }

    @Override
    public UncheckedCompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action,
            Executor executor) {
        return new UncheckedCompletableFuture<T>(future.whenCompleteAsync(action, executor));
    }

    @Override
    public UncheckedCompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn) {
        return new UncheckedCompletableFuture<T>(future.exceptionally(fn));
    }

    @Override
    public CompletableFuture<T> toCompletableFuture() {
        return future.toCompletableFuture();
    }

    /**
     * Returns this UncheckedCompletableFuture.
     *
     * @return this UncheckedCompletableFuture
     */
    public UncheckedCompletableFuture<T> toUncheckedCompletableFuture() {
        return this;
    }

}
