package server.domain.image.dto;

import lombok.Builder;
import lombok.Data;

public class ImageResponseDto {

    @Builder
    @Data
    public static class ImageUploadSuccessResponseDto {
        public Boolean isSuccess;
        public String imgUrl;
    }
}
