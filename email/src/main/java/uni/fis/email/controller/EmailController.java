package uni.fis.email.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import uni.fis.email.dto.EmailRequest;
import uni.fis.email.dto.EmailResponse;
import uni.fis.email.entity.EmailLog;
import uni.fis.email.service.EmailService;
import uni.fis.email.util.JwtUtil;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final JwtUtil jwtUtil; 

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(
            @RequestBody EmailRequest emailRequest,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");

        String role;
        try {
            role = jwtUtil.extractRole(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "Token inválido o expirado",
                            "message", e.getMessage()
                    ));
        }

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "Acceso denegado",
                            "message", "Se requiere rol ADMIN para enviar correos"
                    ));
        }

        EmailResponse response = emailRequest.isHtml()
                ? emailService.sendHtmlEmail(emailRequest)
                : emailService.sendEmail(emailRequest);

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
