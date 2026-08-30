import java.util.ArrayList;
import java.util.Iterator;

/**
 * Stores and provides operations on Monday's tasks.
 */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks tasks to include in this task list
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of this task list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index zero-based index of the task
     * @return the task at the given index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     *
     * @param index zero-based index of the task
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an iterator over the tasks in this list.
     *
     * @return iterator over the tasks
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}