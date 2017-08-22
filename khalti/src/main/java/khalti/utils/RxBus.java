package khalti.utils;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
    private static final RxBus INSTANCE = new RxBus();
    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public static RxBus getInstance() {
        return INSTANCE;
    }

    public <T> Subscription register(final Class<T> eventClass, Action1<T> onNext) {
        return bus
                .filter(event -> event.getClass().equals(eventClass))
                .map(obj -> (T) obj)
                .subscribe(onNext);
    }

    public void post(String tag, Object event) {
        bus.onNext(new Event(event, tag));
    }
}
