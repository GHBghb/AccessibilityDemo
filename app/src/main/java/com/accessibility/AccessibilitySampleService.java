package com.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.accessibility.utils.AccessibilityLog;

/**
 * Created by ghb on 2017/3/6.
 */

@TargetApi(16)
public class AccessibilitySampleService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
        // 获取包名
        String pkgName = event.getPackageName().toString();
        int eventType = event.getEventType();
        AccessibilityOperator.getInstance().updateEvent(this, event);
//        try {
//            Thread.sleep(200);
//        } catch (Exception e) {}
        AccessibilityLog.printLog("eventType: " + eventType + " pkgName: " + pkgName);
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }
}
