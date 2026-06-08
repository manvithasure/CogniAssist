package com.cogniassist.service;

import com.cogniassist.model.SessionData;
import com.cogniassist.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private GeminiAIService geminiAIService;

    public SessionData saveSession(int keystrokeCount,
                                   double typingSpeed, int errorCount) {

        System.out.println("🤖 Asking Gemini AI to analyze fatigue...");

        // Get fatigue level from Gemini AI
        String fatigueLevel = geminiAIService.analyzeFatigue(
                keystrokeCount, typingSpeed, errorCount
        );

        System.out.println("🧠 Fatigue Level from AI: " + fatigueLevel);

        SessionData session = new SessionData(
                keystrokeCount, typingSpeed, errorCount, fatigueLevel
        );
        return sessionRepository.save(session);
    }

    public List<SessionData> getAllSessions() {
        return sessionRepository.findAll();
    }
}