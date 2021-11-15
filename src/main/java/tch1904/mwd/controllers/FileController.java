package tch1904.mwd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Pagination;
import tch1904.mwd.constant.components.response.AppResponse;
import tch1904.mwd.constant.components.response.AppResponseFailure;
import tch1904.mwd.constant.components.response.AppResponseSuccess;
import tch1904.mwd.controllers.request.*;
import tch1904.mwd.entity.FileImg;
import tch1904.mwd.entity.dto.FileMusic;
import tch1904.mwd.services.FileServices;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
public class FileController {
    @Autowired
    FileServices fileServices;

    @PostMapping("/api/user/uploadImg")
    public AppResponse uploadFileImg(UploadFileRequest request) throws Exception {
        try {
            return new AppResponseSuccess(fileServices.addFileImg(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (IOException exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }

    @PostMapping("/api/user/getImagesByUser")
    public AppResponse getImagesByUser(@RequestBody SearchUserImageRequest request) {
        try {
            return new AppResponseSuccess(fileServices.getImagesByUser(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }

    @PostMapping("/api/user/removeFileImg")
    public AppResponse removeFileImg(@RequestBody RemoveFileRequest request) throws Exception {
        try {
            fileServices.removeFileImg(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }

    @GetMapping("unau/imgs/{id}")
    public void getFileImg(@PathVariable String id, HttpServletResponse response) throws IOException {
        FileImg fileImg = fileServices.getFileImg(id);
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileImg.getFileName());
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.getOutputStream().write(fileImg.getImage().getData());
    }


    @PostMapping("/api/user/uploadFileMusic")
    public AppResponse uploadFileMusic(UploadFileRequest request) throws Exception {
        try {
            return new AppResponseSuccess(fileServices.uploadFileMusic(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (IOException exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }

    @PostMapping("/api/user/getListMusicByUser")
    public AppResponse getListMusicByUser(@RequestBody SearchUserMusicRequest request) {
        try {
            return new AppResponseSuccess(fileServices.getListMusicByUser(request.getPagination()));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }

    @PostMapping("/api/user/removeFileMusic")
    public AppResponse removeFileMusic(@RequestBody RemoveFileRequest request) {
        try {
            fileServices.removeFileMusic(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }


    @GetMapping("unau/stream/{id}")
    public void streamMusic(@PathVariable String id, HttpServletResponse response) throws Exception {
        FileMusic fileMusic = fileServices.getFileMusic(id);
        FileCopyUtils.copy(fileMusic.getStream(), response.getOutputStream());
    }



//    @GetMapping("unau/getFile/{id}")
//    public void getFileMusic(@PathVariable String id, HttpServletResponse response) throws Exception {
//        FileMusic fileMusic = fileServices.getFileMusic(id);
//        response.getOutputStream().write(fileMusic.getStream().read());
//    }
}
