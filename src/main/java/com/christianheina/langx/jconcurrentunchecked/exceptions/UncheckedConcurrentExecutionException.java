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

package com.christianheina.langx.jconcurrentunchecked.exceptions;

/**
 * Promise execution exception.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class UncheckedConcurrentExecutionException extends UncheckedConcurrentException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * 
     * @param message
     *            exception message
     */
    public UncheckedConcurrentExecutionException(String message) {
        super(message);
    }

    /**
     * Constructor
     * 
     * @param e
     *            throwable to re-throw
     */
    public UncheckedConcurrentExecutionException(Throwable e) {
        super(e);
    }

    /**
     * Constructor
     * 
     * @param message
     *            exception message
     * @param e
     *            throwable to re-throw
     */
    public UncheckedConcurrentExecutionException(String message, Throwable e) {
        super(message, e);
    }
}
