package com.cogniassist.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "sessions")
public class SessionData {

    @Id
    private String id;
    private int keystrokeCount;
    private double typingSpeed;
    private int errorCount;
    private String fatigueLevel;
    private LocalDateTime timestamp;

    public SessionData() {}

    public SessionData(int keystrokeCount, double typingSpeed,
                       int errorCount, String fatigueLevel) {
        this.keystrokeCount = keystrokeCount;
        this.typingSpeed = typingSpeed;
        this.errorCount = errorCount;
        this.fatigueLevel = fatigueLevel;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public int getKeystrokeCount() { return keystrokeCount; }
    public void setKeystrokeCount(int k) { this.keystrokeCount = k; }
    public double getTypingSpeed() { return typingSpeed; }
    public void setTypingSpeed(double t) { this.typingSpeed = t; }
    public int getErrorCount() { return errorCount; }
    public void setErrorCount(int e) { this.errorCount = e; }
    public String getFatigueLevel() { return fatigueLevel; }
    public void setFatigueLevel(String f) { this.fatigueLevel = f; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime t) { this.timestamp = t; }
}