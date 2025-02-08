package examples;

import com.christianheina.langx.jconcurrentunchecked.UncheckedCompletableFuture;

/**
 * {@link UncheckedCompletableFuture} usage example
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
@SuppressWarnings({ "javadoc", "uncommentedmain" })
public class UncheckedCompletableFutureExample {

    public static void main(String[] args) {
        UncheckedCompletableFuture<Void> promise = UncheckedCompletableFuture.runAsync(() -> {
            System.out.println("First completable future starting asynchronuous run");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("First completable future completing asynchronuous run");
        }).thenRunAsync(() -> {
            System.out.println("Second completable future running asynchronuously after first completed");
        });
        System.out.println("Waiting for completable future to complete");
        promise.get();
        System.out.println("completable future complete");
    }

}
