package com.akbp.racescore.email;

public interface EmailSender {
    boolean sendEmail(Long attemptCount, String to, String subject, String content);
}