package com.example.cloudwang.flyrefresh.flyrefresh;

import android.view.View;

public interface IScrollHandler {
    boolean canScrollUp(View view);
    boolean canScrollDown(View view);
}
