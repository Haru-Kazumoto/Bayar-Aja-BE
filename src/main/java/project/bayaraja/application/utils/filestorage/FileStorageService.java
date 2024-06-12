package project.bayaraja.application.utils.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileStorageService  {
    void init();
    void save(MultipartFile file, String newName);
    Resource load(String fileName);
    void deleteAll();
    Stream<Path> loadAll() throws IOException;
}
