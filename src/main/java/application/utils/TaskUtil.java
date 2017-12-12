package application.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author lancec
 *         Date: 14-2-28
 */
public class TaskUtil {

    /**
     * Async execute a task.
     *
     * @param callable The the callable
     * @return Return the executed Future
     */
    public static <T> Future<T> asyncExecuteTask(Callable callable) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<T> future = service.submit(callable);
        service.shutdown();
        return future;
    }
}
