package techpriest.Url_Shortener.services.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import techpriest.Url_Shortener.services.EmailService;


@Service("javaMailSenderService")
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(String to, String subject, String body) {
       try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("Email sent to {}", to);

        } catch (jakarta.mail.MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new IllegalStateException("Failed to send email to " + to, e);
        }
    }

    @Async
    @Override
    public void sendOtpEmail(String to, String firstName, String otpCode) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("otpCode", otpCode);
        String html = templateEngine.process("email/otp-email", context);
        sendEmail(to, "Your verification code", html);
    }

}
