package tch1904.mwd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tch1904.mwd.authen.JwtProvider;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.response.AppResponse;
import tch1904.mwd.constant.components.response.AppResponseFailure;
import tch1904.mwd.constant.components.response.AppResponseSuccess;
import tch1904.mwd.controllers.request.ApproveRequest;
import tch1904.mwd.controllers.request.FindUsersRequest;
import tch1904.mwd.repository.UserRepository;
import tch1904.mwd.services.CommonServices;
import tch1904.mwd.services.RequestServices;
import tch1904.mwd.services.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CommonServices commonServices;
    @Autowired
    private RequestServices requestServices;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/getUserList")
    public AppResponse getUserList(@RequestBody FindUsersRequest request) {
        try {
            return new AppResponseSuccess(userService.getUserList(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/approveAddMoney")
    public AppResponse approveAddMoney(@RequestBody ApproveRequest request) {
        try {
            userService.approveAddMoney(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/approveRegisterSinger")
    public AppResponse approveRegisterSinger(@RequestBody ApproveRequest request) {
        try {
            userService.approveRegisterSinger(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/approvePublishProduct")
    public AppResponse approvePublishProduct(@RequestBody ApproveRequest request) {
        try {
            userService.approvePublishProduct(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }


}
