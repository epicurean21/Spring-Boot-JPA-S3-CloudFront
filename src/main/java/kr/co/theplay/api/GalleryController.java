package kr.co.theplay.api;

import kr.co.theplay.dto.GalleryDto;
import kr.co.theplay.service.GalleryService;
import kr.co.theplay.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class GalleryController {
    private S3Service s3Service;
    private GalleryService galleryService;

    @GetMapping("/gallery")
    public String dispWrite(Model model) {
        List<GalleryDto> galleryDtoList = galleryService.getList();
        model.addAttribute("galleryList", galleryDtoList);
        return "gallery.html";
    }

    @PostMapping("/gallery")
    public String execWrite(GalleryDto galleryDto, MultipartFile file) throws IOException {
        String imgPath = s3Service.upload(galleryDto.getFilePath(), file);
        /*
        service.upload()를 호출할 때, 기존의 파일명을 파라미터로 전달합니다.
        이 값은 gallery.html에 hidden 값으로 정의되어 있습니다.
         */
        galleryDto.setFilePath(imgPath);
        galleryService.savePost(galleryDto);

        return "redirect:/gallery.html";
    }

    @DeleteMapping("/gallery/{cloudFront}/{filePath}")
    public String execErase(@PathVariable String cloudFront, @PathVariable String filePath) throws IOException {
        //String imgFilePath = filePath.substring(s3Service.CLOUD_FRONT_DOMAIN_NAME.length());
        s3Service.delete(filePath);
        galleryService.deletePost(filePath);
        return "redirect:/gallery.html";
    }

}
