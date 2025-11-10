package uni.fis.email.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.email.dto.EmailRequest;
import uni.fis.email.dto.EmailResponse;
import uni.fis.email.entity.EmailLog;
import uni.fis.email.service.EmailService;

import java.util.List;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest) {
        EmailResponse response;
        
        if (emailRequest.isHtml()) {
            response = emailService.sendHtmlEmail(emailRequest);
        } else {
            response = emailService.sendEmail(emailRequest);
        }
        
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<EmailLog>> getAllLogs() {
        return ResponseEntity.ok(emailService.getAllEmailLogs());
    }

    @GetMapping("/logs/recipient/{recipient}")
    public ResponseEntity<List<EmailLog>> getLogsByRecipient(@PathVariable String recipient) {
        return ResponseEntity.ok(emailService.getEmailLogsByRecipient(recipient));
    }

    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<List<EmailLog>> getLogsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(emailService.getEmailLogsByUserId(userId));
    }

    @GetMapping("/logs/status/{status}")
    public ResponseEntity<List<EmailLog>> getLogsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(emailService.getEmailLogsByStatus(status));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Email service is running");
    }
}