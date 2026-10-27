package io.freemytv;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Sees the remote's keys before any app does, so the app buttons work from inside Netflix, Disney and the rest. The
 * system waits for it on every key (up to half a second), so it runs in a process of its own and does next to nothing.
 */
public class RemoteKeyService extends AccessibilityService {
    @Override protected boolean onKeyEvent(KeyEvent event) {
        // Menu (F9) on the home screen: the focused app's context menu. Elsewhere the TV keeps its own handler.
        if (event.getKeyCode() == KeyEvent.KEYCODE_F9 && RemoteKeys.KALLEY
                && HomeActivity.class.getName().equals(RemoteKeys.topWindow)) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                startActivity(new Intent(this, HomeActivity.class)
                        .putExtra(HomeActivity.EXTRA_APP_MENU, true)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            return true;
        }
        return RemoteKeys.onKey(this, event);
    }

    /** The window in front: the R10G's Input, Settings, Guide and Profile work only over our home screen. */
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && event.getClassName() != null) {
            RemoteKeys.topWindow = event.getClassName().toString();
        }
    }

    @Override public void onInterrupt() {}
}
