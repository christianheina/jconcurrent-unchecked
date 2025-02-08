package examples;

import com.christianheina.langx.jconcurrentunchecked.UncheckedFuture;

/**
 * {@link UncheckedFuture} usage example
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
@SuppressWarnings({ "javadoc", "uncommentedmain" })
public class UncheckedFutureExample {

    public static void main(String[] args) {
        UncheckedFuture<Void> promise = UncheckedFuture.runAsync(() -> {
            System.out.println("future starting asynchronuous run");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future completing asynchronuous run");
        });
        System.out.println("Waiting for future to complete");
        promise.get();
        System.out.println("future complete");
    }

}
