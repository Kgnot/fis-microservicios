package uni.fis.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    
    private Integer userId; 
    private String to;
    private String subject;
    private String body;
    private boolean isHtml;
}