package me.devilsen.valve;

import java.util.List;

/**
 * desc :
 * date : 2019/3/6
 *
 * @author : dongSen
 */
public interface ValveController<T> {

    void add(T t);

    void addAll(List<T> list);

    void postDelay();

    void postImmediate();

}
