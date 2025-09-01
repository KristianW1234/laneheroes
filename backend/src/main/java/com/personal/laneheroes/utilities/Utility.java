package com.personal.laneheroes.utilities;

import com.personal.laneheroes.response.ResponseWrapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class Utility {
    public static Pageable setupPageable(int page, int size, String sortBy, String sortOrder){
        Sort sort = Sort.by(sortBy);
        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        return PageRequest.of(page, size, sort);
    }

    public static ResponseWrapper<String> uploadFile(MultipartFile file, String imageDir, String folder) {
        ResponseWrapper<String> result = new ResponseWrapper<String>();
        try {
            String absolutePath = imageDir + folder;
            folderCheckAdd(absolutePath);
            File file2 = new File(absolutePath + "/" + file.getOriginalFilename());
            String ext = FilenameUtils.getExtension(file2.getAbsolutePath());
            if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg")) {
                InputStream is;
                FileOutputStream os = null;
                try {
                    is = file.getInputStream();
                    os = new FileOutputStream(file2);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                } finally {
                    os.close();
                }
                result.setStatus(ResponseMessages.SUCCESS_STATUS);
                result.setMessage(ResponseMessages.IMAGE_UPLOAD_SUCCESS);
                result.setData(file.getOriginalFilename());

            } else {
                result.setStatus(ResponseMessages.FAIL_STATUS);
                result.setMessage("Picture not in PNG/JPG format");
            }
        } catch (IOException ex) {
            result.setStatus(ResponseMessages.FAIL_STATUS);
            result.setMessage("IO Exception caught: " + ex.getMessage());


        }
        return result;
    }

    public static void folderCheckAdd(String path) throws IOException{
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static <T, ID> Optional<T> getValidEntityById(
            JpaRepository<T, ID> repository,
            ID id,
            String entityName,
            String actionName,
            ResponseWrapper<?>[] responseHolder
    ) {
        Optional<T> result = repository.findById(id);
        if (result.isEmpty()) {
            responseHolder[0] = new ResponseWrapper<>(
                    entityName + " " + actionName + ": ID is invalid",
                    ResponseMessages.FAIL_STATUS,
                    null
            );
        }
        return result;
    }

    public static File saveTempFile(MultipartFile multipartFile) throws IOException {
        File temp = File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(temp);
        temp.deleteOnExit(); // Optional: deletes file on JVM shutdown
        return temp;
    }
}
