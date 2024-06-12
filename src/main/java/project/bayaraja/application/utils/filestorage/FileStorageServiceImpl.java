package project.bayaraja.application.utils.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import project.bayaraja.application.exceptions.FileStorageException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_DIR = "upload_images";
    private static final String FAILED_CREATE_FOLDER_MSG = "Failed to create folder";
    private static final String DUPLICATE_FILE_MSG = "Duplicate file name";
    private static final String COULD_NOT_READ_FILE_MSG = "Couldn't read file!";
    private static final String COULD_NOT_LOAD_FILES_MSG = "Could not load the files!";
    private static final String ERROR_LOAD_FILE_MSG = "Error loading file: ";

    private final Path root = Paths.get(UPLOAD_DIR);

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new FileStorageException(FAILED_CREATE_FOLDER_MSG, e);
        }
    }

    @Override
    public void save(MultipartFile file, String newName) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(newName), StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            throw new FileStorageException(DUPLICATE_FILE_MSG, e);
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    @Override
    public Resource load(String fileName) {
        try {
            Path file = this.root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new FileStorageException(COULD_NOT_READ_FILE_MSG);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new FileStorageException(ERROR_LOAD_FILE_MSG + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(this.root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new FileStorageException(COULD_NOT_LOAD_FILES_MSG, e);
        }
    }
}
