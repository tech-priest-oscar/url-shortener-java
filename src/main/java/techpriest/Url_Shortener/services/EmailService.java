package techpriest.Url_Shortener.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);

    void sendOtpEmail(String to, String firstName, String otpCode);
}
