package controller;

public interface IController<E> {
    public E[] findByName(String name) throws Exception;

    public E[] findAll() throws Exception;

    public int create(E entity) throws Exception;

    public boolean update(E entity) throws Exception;

    public boolean delete(int id) throws Exception;
}
