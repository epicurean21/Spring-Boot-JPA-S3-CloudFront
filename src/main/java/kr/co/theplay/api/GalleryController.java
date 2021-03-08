package kr.co.theplay.api;

import kr.co.theplay.dto.GalleryDto;
import kr.co.theplay.service.GalleryService;
import kr.co.theplay.service.S3Service;
import kr.co.theplay.service.common.ResponseService;
import kr.co.theplay.service.common.model.CommonResult;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ResponseService responseService;

    @GetMapping("/gallery")
    public String dispWrite(Model model) {
        List<GalleryDto> galleryDtoList = galleryService.getList();
        model.addAttribute("galleryList", galleryDtoList);
        return "gallery.html";
    }

    @PostMapping("/gallery")
    public String execWrite(GalleryDto galleryDto, MultipartFile file) throws IOException {
        String imgPath = s3Service.upload(galleryDto.getFilePath(), file);
        galleryDto.setFilePath(imgPath);
        galleryService.savePost(galleryDto);


        /*String imgPath = s3Service.upload(galleryDto.getFilePath(), file);
        galleryDto.setFilePath(imgPath);
        galleryService.savePost(galleryDto);*/

        /*
        service.upload()를 호출할 때, 기존의 파일명을 파라미터로 전달합니다.
        이 값은 gallery.html에 hidden 값으로 정의되어 있습니다.
         */


        return "redirect:/gallery.html";
    }

    @PostMapping("/upload")
    public ResponseEntity<CommonResult> uploadMultipleFiles(GalleryDto galleryDto, MultipartFile[] file) throws IOException {
        for (int i = 0; i < file.length; i++) {
            String imgPath = s3Service.uploadMultipleFiles(galleryDto.getFilePath(), file[i]);
            galleryDto.setFilePath(imgPath);
            galleryService.savePost(galleryDto);
        }
        return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.OK);
    }
    /*@PostMapping("/upload")
    public String uploadMultipleFiles(GalleryDto galleryDto, MultipartFile file) throws IOException {

        List<String> imgPath = s3Service.uploadMultipleFiles(galleryDto, file);
        for (int i = 0; i < imgPath.size(); i++) {
            galleryDto[i].setFilePath(imgPath.get(i));
            galleryService.savePost(galleryDto[i]);
        }
        *//*String imgPath = s3Service.upload(galleryDto.getFilePath(), file);
        galleryDto.setFilePath(imgPath);
        galleryService.savePost(galleryDto);*//*

     *//*
        service.upload()를 호출할 때, 기존의 파일명을 파라미터로 전달합니다.
        이 값은 gallery.html에 hidden 값으로 정의되어 있습니다.
         *//*


        return "Success";
    }*/

    /*@PostMapping("/upload")
    public String uploadMultipleFiles(GalleryDto[] galleryDto, MultipartFile[] file) throws IOException {

        List<String> imgPath = s3Service.uploadMultipleFiles(galleryDto, file);
        for (int i = 0; i < imgPath.size(); i++) {
            galleryDto[i].setFilePath(imgPath.get(i));
            galleryService.savePost(galleryDto[i]);
        }
        *//*String imgPath = s3Service.upload(galleryDto.getFilePath(), file);
        galleryDto.setFilePath(imgPath);
        galleryService.savePost(galleryDto);*//*

     *//*
        service.upload()를 호출할 때, 기존의 파일명을 파라미터로 전달합니다.
        이 값은 gallery.html에 hidden 값으로 정의되어 있습니다.
         *//*


        return "Success";
    }*/

    @DeleteMapping("/gallery/{cloudFront}/{filePath}")
    public String execErase(@PathVariable String cloudFront, @PathVariable String filePath) throws IOException {
        //String imgFilePath = filePath.substring(s3Service.CLOUD_FRONT_DOMAIN_NAME.length());
        s3Service.delete(filePath);
        galleryService.deletePost(filePath);
        return "redirect:/gallery.html";
    }

}
