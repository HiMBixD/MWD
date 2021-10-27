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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FileController {
    @Autowired
    FileServices fileServices;

    @PostMapping("/uploadImg")
    public AppResponse uploadFileImg(UploadFileRequest request) throws Exception {
        try {
            return new AppResponseSuccess(fileServices.addFileImg(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (IOException exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }

    @GetMapping("/imgs/{id}")
    public String getFileImg(@PathVariable String id, Model model) {
        FileImg fileImg = fileServices.getFileImg(id);
        model.addAttribute("description", fileImg.getDescription());
        model.addAttribute("image",
                Base64.getEncoder().encodeToString(fileImg.getImage().getData()));
        return "imgs";
    }

    @PostMapping("/uploadMusic")
    public AppResponse uploadFileMusic(UploadFileRequest request) throws Exception {
        try {
            return new AppResponseSuccess(fileServices.addFileMusic(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (IOException exception) {
            return new AppResponseFailure(exception.getMessage());
        }
    }


    @GetMapping("/musics/{id}")
    public String getFileMusic(@PathVariable String id, Model model) throws Exception {
        FileMusic fileMusic = fileServices.getFileMusic(id);
        model.addAttribute("description", fileMusic.getDescription());
        model.addAttribute("url", "/musics/stream/" + id);
        return "musics";
    }

    @GetMapping("/musics/stream/{id}")
    public void streamMusic(@PathVariable String id, HttpServletResponse response) throws Exception {
        FileMusic fileMusic = fileServices.getFileMusic(id);
        FileCopyUtils.copy(fileMusic.getStream(), response.getOutputStream());
    }
}
