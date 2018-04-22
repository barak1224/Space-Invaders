package io;

/**
 * The interface for Task.
 * @author Barak Talmor
 * @param <T> - the generic object for task
 */
public interface Task<T> {
    /**
     * The method runs the object.
     * @return the object.
     */
    T run();
 }