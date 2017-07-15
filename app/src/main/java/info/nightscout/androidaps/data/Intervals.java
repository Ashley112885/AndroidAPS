package info.nightscout.androidaps.data;

import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

import info.nightscout.androidaps.interfaces.Interval;

/**
 * Created by mike on 09.05.2017.
 */

// Zero duration means end of interval

public abstract class Intervals<T extends Interval> {

    LongSparseArray<T> rawData = new LongSparseArray<>(); // oldest at index 0

    public Intervals reset() {
        rawData = new LongSparseArray<>();
        return this;
    }

    public void add(T newInterval) {
        rawData.put(newInterval.start(), newInterval);
        merge();
    }

    protected abstract void merge();

    public void add(List<T> list) {
        for (T interval : list) {
            rawData.put(interval.start(), interval);
        }
        merge();
    }



    public List<T> getList() {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < rawData.size(); i++)
            list.add(rawData.valueAt(i));
        return list;
    }

    public List<T> getReversedList() {
        List<T> list = new ArrayList<>();
        for (int i = rawData.size() -1; i>=0; i--)
            list.add(rawData.valueAt(i));
        return list;
    }

    protected int binarySearch(long value) {
        int lo = 0;
        int hi = rawData.size() - 1;

        while (lo <= hi) {
            final int mid = (lo + hi) >>> 1;
            final Interval midVal = rawData.valueAt(mid);

            if (midVal.before(value)) {
                lo = mid + 1;
            } else if (midVal.after(value)) {
                hi = mid - 1;
            } else if (midVal.match(value)) {
                return mid;  // value found
            }
        }
        return ~lo;  // value not present
    }

    public abstract T getValueByInterval(long time);

    public int size() {
        return rawData.size();
    }

    public T get(int index) {
        return rawData.valueAt(index);
    }

    public T getReversed(int index) {
        return rawData.valueAt(size() - 1 - index);
    }



}