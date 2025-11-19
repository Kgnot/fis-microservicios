package uni.fis.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fis.email.dto.EmailRequest;
import uni.fis.email.dto.EmailResponse;
import uni.fis.email.entity.EmailLog;
import uni.fis.email.repository.EmailLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Override
    @Transactional
    public EmailResponse sendEmail(EmailRequest emailRequest) {
        EmailLog emailLog = EmailLog.builder()
                .id_usuario_receptor(emailRequest.getUserId())
                .recipiente(emailRequest.getTo())
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .build();

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getTo());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getBody());

            mailSender.send(message);

            emailLog.setStatus("SENT");
            emailLog = emailLogRepository.save(emailLog);

            log.info("Email enviado exitosamente a: {}", emailRequest.getTo());

            return EmailResponse.builder()
                    .success(true)
                    .message("Email enviado exitosamente")
                    .logId(emailLog.getId())
                    .timestamp(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            emailLog.setStatus("FAILED");
            emailLog.setErrorMessage(e.getMessage());
            emailLog = emailLogRepository.save(emailLog);

            log.error("Error al enviar email a: {}", emailRequest.getTo(), e);

            return EmailResponse.builder()
                    .success(false)
                    .message("Error al enviar email: " + e.getMessage())
                    .logId(emailLog.getId())
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    @Transactional
    public EmailResponse sendHtmlEmail(EmailRequest emailRequest) {
        EmailLog emailLog = EmailLog.builder()
                .id_usuario_receptor(emailRequest.getUserId())
                .recipiente(emailRequest.getTo())
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .build();

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getBody(), true);

            mailSender.send(mimeMessage);

            emailLog.setStatus("SENT");
            emailLog = emailLogRepository.save(emailLog);

            log.info("Email HTML enviado exitosamente a: {}", emailRequest.getTo());

            return EmailResponse.builder()
                    .success(true)
                    .message("Email HTML enviado exitosamente")
                    .logId(emailLog.getId())
                    .timestamp(LocalDateTime.now())
                    .build();

        } catch (MessagingException e) {
            emailLog.setStatus("FAILED");
            emailLog.setErrorMessage(e.getMessage());
            emailLog = emailLogRepository.save(emailLog);

            log.error("Error al enviar email HTML a: {}", emailRequest.getTo(), e);

            return EmailResponse.builder()
                    .success(false)
                    .message("Error al enviar email HTML: " + e.getMessage())
                    .logId(emailLog.getId())
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public List<EmailLog> getAllEmailLogs() {
        return emailLogRepository.findAll();
    }

    @Override
    public List<EmailLog> getEmailLogsByRecipient(String recipient) {
        return emailLogRepository.findByRecipient(recipient);
    }

    @Override
    public List<EmailLog> getEmailLogsByStatus(String status) {
        return emailLogRepository.findByStatus(status);
    }

    @Override
    public List<EmailLog> getEmailLogsByUserId(Long userId) {
        return emailLogRepository.findByUserId(userId);
    }
}