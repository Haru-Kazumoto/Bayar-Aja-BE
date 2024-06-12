package project.bayaraja.application.services.upload_image;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import project.bayaraja.application.utils.filestorage.FileStorageService;

import java.io.IOException;

@RestController @RequestMapping(path = "/uploads")
@RequiredArgsConstructor
public class UploadImageController {

    private final FileStorageService fileStorageService;

    @RequestMapping(
            path = "/{filename}",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String filename) throws IOException {
        Resource imgFile = this.fileStorageService.load(filename);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(imgFile.getInputStream()));
    }

}