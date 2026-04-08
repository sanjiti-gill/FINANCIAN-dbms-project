package dao;

import java.util.List;

/**
 * Interface for database operations - uses String IDs to match VARCHAR schema.
 */
public interface DatabaseOperations<T> {
    boolean  add(T obj);
    List<T>  getAll();
    T        getById(String id);
    boolean  update(T obj);
    boolean  delete(String id);
}
