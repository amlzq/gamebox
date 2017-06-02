package com.gamebox_idtkown.services;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.gamebox_idtkown.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/12/27.
 */

public class InstallAccessibilityService extends AccessibilityService {
    Map<Integer, Boolean> handledMap = new HashMap<>();

    public InstallAccessibilityService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            int eventType = event.getEventType();
            if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                    eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                if (handledMap.get(event.getWindowId()) == null) {
                    boolean handled = iterateNodesAndHandle(nodeInfo);
                    if (handled) {
                        handledMap.put(event.getWindowId(), true);
                    }
                }
            }
        }
    }

    private boolean iterateNodesAndHandle(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            String nodeName = nodeInfo.getClassName()+"";
//            || "android.widget.TextView".equals(nodeName)
            if ("android.widget.Button".equals(nodeName) ) {
                String nodeCotent = nodeInfo.getText()+"";
                LogUtil.msg("content is: " + nodeCotent);
                if (nodeCotent.contains("安装") || nodeCotent.contains("完成")||nodeCotent.contains("确定")) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
            //遇到ScrollView的时候模拟滑动一下
            else if ("android.widget.ScrollView".equals(nodeInfo.getClassName())) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }

            int childCount = nodeInfo.getChildCount();
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo childNodeInfo = nodeInfo.getChild(i);
                if (iterateNodesAndHandle(childNodeInfo)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onInterrupt() {
    }
}
