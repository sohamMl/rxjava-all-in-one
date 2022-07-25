import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * This is an example of illustration of the video series RxJava All In One.
 * <p>
 * You can watch the video implementation of this source code for free on YouTube here:
 * https://youtu.be/cZRodTYKlUs
 * Subscribe here -> http://bit.ly/MithuRoyOnYoutube
 * <p>
 * We've created Cold Observable using Observable.just()
 * and paused the thread to prove whether it is really Cold Observable
 * and we also created Hot Observable using by converting a Cold Observable
 * to a Hot Observable using Connectable Observable
 * <p>
 * Created By Mithu Roy on 16/02/2019
 */

public class Main {
    public static void main(String[] args) {
        createColdObservable();
        createHotAndConnectableObservable();
    }

    /**
     * Creates Cold Observable using Observable.just()
     * Because each and every onNext() gets their separate emission
     */
    private static void createColdObservable() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);

        observable.subscribe(item -> System.out.println("Observer 1: " + item));

        pause(3000);

        observable.subscribe(item -> System.out.println("Observer 2: " + item));
    }

    /**
     * Creates a Hot Observable
     * The moment we call the publish() method on Observable.just()
     * It Converts the Observable to a Connectable Observable
     * Connectable Observable doesn't start it's emission right after you subscribe
     * The moment we call connect() method it starts emission
     * Any Observer which subscribes after calling connect() misses emissions
     */
//    private static void createHotAndConnectableObservable() {
//        ConnectableObservable<Integer> observable = Observable.just(1, 2, 3, 4, 5).publish();
//
//        observable.subscribe(item -> System.out.println("Observer 1: " + item));
//    }

    private static void createHotAndConnectableObservable() {

        Observable observable = Observable.interval(1, TimeUnit.SECONDS);
        observable.subscribe(System.out::println);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



        Observable<Integer> ob = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for(int i=0;i<15;i++){
                    emitter.onNext(i);
                    Thread.sleep(1000);
                }
            }
        });


//        ob.subscribe(item -> System.out.println("sub 1 : "+item));

//        ob.subscribe(item -> System.out.println("sub 2 : "+item));

        ConnectableObservable<Integer> ConnectOb = ob.publish();

        ConnectOb.subscribe(item -> System.out.println("sub 1 : "+item));
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConnectOb.subscribe(item -> System.out.println("sub 2 : "+item));

        ConnectOb.connect();


    }

    /**
     * This method sleep the main thread for specified duration
     *
     * @param duration Sleep Duration in Milliseconds
     */
    private static void pause(int duration) {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
