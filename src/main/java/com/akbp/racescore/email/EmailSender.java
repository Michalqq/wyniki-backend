package com.akbp.racescore.email;

public interface EmailSender {
    boolean sendEmail(String to, String subject, String content);
}