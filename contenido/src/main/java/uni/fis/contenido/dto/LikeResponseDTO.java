package uni.fis.contenido.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResponseDTO {
    private Integer publicacionId;
    private Integer totalLikes;
}
