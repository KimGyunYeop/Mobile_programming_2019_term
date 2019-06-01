package com.MP2019.NDND;

import android.support.annotation.Nullable;

public final class CalendarPresenter {

    @Nullable
    private CalendarView view;

    public CalendarPresenter(@Nullable CalendarView view) {
        this.view = view;
    }

    public void addTextView() {
        if (null != getView()) {
            getView().prepareTextView();
        }
    }

    public void addCalendarView() {
        if (null != getView()) {
            getView().prepareCalendarView();
        }
    }

    public void detachView() {
        this.view = null;
    }

    @Nullable
    public CalendarView getView() {
        return view;
    }
}
