package tch1904.mwd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Message;
import tch1904.mwd.constant.components.response.AppResponse;
import tch1904.mwd.constant.components.response.AppResponseFailure;
import tch1904.mwd.constant.components.response.AppResponseSuccess;
import tch1904.mwd.controllers.request.UploadFileRequest;
import tch1904.mwd.entity.FileImg;
import tch1904.mwd.entity.dto.FileMusic;
import tch1904.mwd.services.FileServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@RestController
@CrossOrigin
public class FileController {
    @Autowired
    FileServices fileServices;

    @PostMapping("/api/uploadImg")
    public AppResponse uploadFileImg(UploadFileRequest request) throws Exception {
        try {
            return new AppResponseSuccess(fileServices.addFileImg(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (IOException exception) {
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


    @PostMapping("/api/uploadFileMusic")
    public AppResponse uploadFileMusic(UploadFileRequest request) throws Exception {
        try {
            return new AppResponseSuccess(fileServices.uploadFileMusic(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (IOException exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }


    @GetMapping("unau/stream/{id}")
    public void streamMusic(@PathVariable String id, HttpServletResponse response) throws Exception {
        FileMusic fileMusic = fileServices.getFileMusic(id);
        FileCopyUtils.copy(fileMusic.getStream(), response.getOutputStream());
    }

    @GetMapping("unau/getFile/{id}")
    public void getFileMusic(@PathVariable String id, HttpServletResponse response) throws Exception {
        FileMusic fileMusic = fileServices.getFileMusic(id);
        response.getOutputStream().write(fileMusic.getStream().read());
    }
}
