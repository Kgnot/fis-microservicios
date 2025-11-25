package fis.auth.domain.model;

public record EmailRequest(
        Integer userId,
        String to,
        String subject,
        String body,
        boolean isHtml
) {
}
