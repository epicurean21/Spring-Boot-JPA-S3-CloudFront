package kr.co.theplay.dto;

import kr.co.theplay.domain.entity.GalleryEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GalleryDto {
    private Long id;
    private String filePath;
    private String imgFullPath;

    public GalleryEntity toEntity() {
        GalleryEntity build = GalleryEntity.builder()
                .id(id)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public GalleryDto(Long id, String filePath, String imgFullPath) {
        this.id = id;
        this.filePath = filePath;
        this.imgFullPath = imgFullPath;
    }
}
