package fis.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmailRequest(
                Integer userId,
                String to,
                String subject,
                String body,
                @JsonProperty("isHtml") Boolean html) {

        public boolean isHtml() {
                return Boolean.TRUE.equals(html);
        }
}
