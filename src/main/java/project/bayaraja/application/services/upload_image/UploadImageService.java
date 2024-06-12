package project.bayaraja.application.services.upload_image;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImageService {
    Resource getImage(String filename);
    String saveFileToDisk(MultipartFile file);
}
