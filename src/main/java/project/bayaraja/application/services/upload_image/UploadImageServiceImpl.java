package project.bayaraja.application.services.upload_image;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.bayaraja.application.utils.filestorage.FileStorageService;
import project.bayaraja.application.utils.filestorage.FilenameUtils;

@Service @RequiredArgsConstructor
public class UploadImageServiceImpl implements UploadImageService{

    private final FileStorageService fileStorageService;

    @Override
    public Resource getImage(String filename) {
        return this.fileStorageService.load(filename);
    }

    @Override
    public String saveFileToDisk(MultipartFile file) {
        String extension = FilenameUtils.getExtensionByStringHandling(file.getOriginalFilename()).get();
        String newFileName = FilenameUtils.getRandomName() + "." + extension;

        this.fileStorageService.save(file,newFileName);

        return newFileName;
    }
}
