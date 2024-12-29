package raf.rs.notification.service;

public interface EmailService {

    void sendMessage(String to, String subject, String body);

}
