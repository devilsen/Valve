package me.devilsen.valve;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.List;

/**
 * desc : 流量控制阀（避免消息过多，造成频繁刷新）
 * date : 2019/3/6
 *
 * @author : dongSen
 */
public class Valve<T> implements ValveController<T> {

    private static final int DEFAULT_MAX_SIZE = 200;
    private static final int DEFAULT_DELAY_TIME = 100;
    private static final int MESSAGE_WHAT_POST = 930116;

    private int size;
    private final Spanner mSpanner;

    private int mMaxSize;
    private int mDelayTime;

    public Valve(Spanner spanner) {
        this(DEFAULT_MAX_SIZE, DEFAULT_DELAY_TIME, spanner);
    }

    public Valve(int maxSize, Spanner spanner) {
        this(maxSize, DEFAULT_DELAY_TIME, spanner);
    }

    public Valve(int maxSize, int delayTime, Spanner spanner) {
        this.mMaxSize = maxSize;
        this.mDelayTime = delayTime;
        this.mSpanner = spanner;
    }

    @Override
    public void add(T message) {
        size++;

        post();
    }

    @Override
    public void addAll(List<T> list) {
        if (list == null)
            return;
        size += list.size();

        post();
    }

    private void post() {
        if (size > mMaxSize) {
            postImmediate();
        } else {
            postDelay();
        }
    }

    @Override
    public void postDelay() {
        mHandler.removeMessages(MESSAGE_WHAT_POST);
        mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT_POST, mDelayTime);
    }

    @Override
    public void postImmediate() {
        mHandler.removeMessages(MESSAGE_WHAT_POST);
        mSpanner.open();
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            postImmediate();
            return false;
        }
    });

}
