package com.cogniassist.controller;

import com.cogniassist.model.SessionData;
import com.cogniassist.service.KeystrokeTracker;
import com.cogniassist.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private KeystrokeTracker keystrokeTracker;

    // Dashboard page
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // Start tracking session
    @PostMapping("/api/session/start")
    @ResponseBody
    public String startSession() {
        keystrokeTracker.startSession();
        return "✅ Tracking session started!";
    }

    // Stop session and save to MongoDB
    @PostMapping("/api/session/stop")
    @ResponseBody
    public SessionData stopSession() {
        String summary = keystrokeTracker.stopSession();
        System.out.println(summary);
        return sessionService.saveSession(
                keystrokeTracker.getKeystrokeCount(),
                keystrokeTracker.getTypingSpeed(),
                keystrokeTracker.getErrorCount()
        );
    }

    // Get all sessions
    @GetMapping("/api/session/all")
    @ResponseBody
    public List<SessionData> getAllSessions() {
        return sessionService.getAllSessions();
    }

    // Test endpoint
    @GetMapping("/api/session/test")
    @ResponseBody
    public String test() {
        return "✅ CogniAssist API is Running!";
    }
    @GetMapping("/api/session/counts")
    @ResponseBody
    public Map<String, Integer> getCounts() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("keystrokeCount", keystrokeTracker.getKeystrokeCount());
        counts.put("errorCount", keystrokeTracker.getErrorCount());
        return counts;
    }
}