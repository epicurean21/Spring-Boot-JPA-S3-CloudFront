package kr.co.theplay.domain.repository;

import kr.co.theplay.domain.entity.GalleryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<GalleryEntity, Long> {
    @Override
    List<GalleryEntity> findAll();

    Optional<GalleryEntity> findByFilePath(String filePath);
}
