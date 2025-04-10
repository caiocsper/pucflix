package view;

public interface IMenu <T> {
    public void menu();
    public void findByName();
    public T findByName(String message);
    public void findAll();
    public void create();
    public void update();
    public void delete();
    public void read(T entity);
    public void displayHeader();
}
