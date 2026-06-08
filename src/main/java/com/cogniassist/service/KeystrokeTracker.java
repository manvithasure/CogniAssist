package com.cogniassist.service;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.springframework.stereotype.Service;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class KeystrokeTracker implements NativeKeyListener {

    private int keystrokeCount = 0;
    private int errorCount = 0;
    private long sessionStartTime;
    private boolean sessionActive = false;

    // Start global tracking
    public void startSession() {
        keystrokeCount = 0;
        errorCount = 0;
        sessionStartTime = System.currentTimeMillis();
        sessionActive = true;

        try {
            // Suppress JNativeHook logs
            Logger logger = Logger.getLogger(
                    GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);

            // Register global keyboard hook
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            System.out.println("⌨️ Global keyboard tracking started!");

        } catch (NativeHookException e) {
            System.out.println("❌ Hook error: " + e.getMessage());
        }
    }

    // Stop global tracking
    public String stopSession() {
        sessionActive = false;

        try {
            GlobalScreen.removeNativeKeyListener(this);
            GlobalScreen.unregisterNativeHook();
            System.out.println("⏹ Global keyboard tracking stopped!");
        } catch (NativeHookException e) {
            System.out.println("❌ Unhook error: " + e.getMessage());
        }

        double speed = getTypingSpeed();
        return String.format(
                "Session ended — Keystrokes: %d, Errors: %d, Speed: %.1f keys/min",
                keystrokeCount, errorCount, speed
        );
    }

    // Called automatically for every key press on computer
    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!sessionActive) return;

        int keyCode = event.getKeyCode();

        // Backspace or Delete = error
        if (keyCode == NativeKeyEvent.VC_BACKSPACE ||
                keyCode == NativeKeyEvent.VC_DELETE) {
            errorCount++;
            System.out.println("❌ Error key! Total errors: "
                    + errorCount);
        } else {
            keystrokeCount++;
            System.out.println("⌨️ Key pressed! Total: "
                    + keystrokeCount);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    // Calculate typing speed
    public double getTypingSpeed() {
        if (!sessionActive) return 0;
        long elapsedMs = System.currentTimeMillis() - sessionStartTime;
        double elapsedMinutes = elapsedMs / 60000.0;
        if (elapsedMinutes == 0) return 0;
        return keystrokeCount / elapsedMinutes;
    }

    public int getKeystrokeCount() { return keystrokeCount; }
    public int getErrorCount() { return errorCount; }
    public boolean isSessionActive() { return sessionActive; }
}