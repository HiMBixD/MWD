package tch1904.mwd.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tch1904.mwd.constant.AppConstants;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Message;
import tch1904.mwd.constant.components.response.AppResponse;
import tch1904.mwd.constant.components.response.AppResponseFailure;
import tch1904.mwd.constant.components.response.AppResponseSuccess;
import tch1904.mwd.controllers.request.ActiveAccountRequest;
import tch1904.mwd.controllers.request.AddMoneyRequest;
import tch1904.mwd.controllers.request.ChangePasswordRequest;
import tch1904.mwd.controllers.request.UpdateAccountRequest;
import tch1904.mwd.entity.dto.UserDTO;
import tch1904.mwd.services.CommonServices;
import tch1904.mwd.services.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UmCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(UmCommonController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private CommonServices commonServices;

    @PostMapping("/getMyInfo")
    public AppResponse getMyInfo() {
        try {
            UserDTO userDTO = userService.getUserInfo(commonServices.getCurrentUser().getUsername());
            assert userDTO != null;
            return new AppResponseSuccess(userDTO);
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/sendOtp")
    public AppResponse sendOtp() {
        try {
            UserDTO userDTO = userService.getUserInfo(commonServices.getCurrentUser().getUsername());
            assert userDTO != null;
            String code = commonServices.randomString(6);
            userService.saveOtp(code, userDTO.getUserName());
            String mailContent = "<p><strong><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Dear Mr/Mrs " + userDTO.getFullName() + ",</span></span></span></strong></p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Your OTP code is: " + "<strong>" + code + "</strong>" + "</span></span></span></p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Thank you for using our service,</span></span></span></p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Best regards.</span></span></span></p>";
            String mailHeader = "OTP confirm on Mwd website";
            commonServices.sendEmail(userDTO.getEmail(), mailContent, mailHeader);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/active")
    public AppResponse activeAccount(@RequestBody ActiveAccountRequest request) {
        try {
            if (StringUtils.isEmpty(request.getOtp())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Otp"));
            }
            userService.activeAccount(request, commonServices.getCurrentUser().getUsername());
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/updateProfile")
    public AppResponse updateProfile(@RequestBody UpdateAccountRequest request) {
        try {
            if (StringUtils.isEmpty(request.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "username"));
            }
            if (StringUtils.isEmpty(request.getEmail())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "email"));
            }
            if (StringUtils.isEmpty(request.getPhone())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "phone"));
            }
            if (StringUtils.isEmpty(request.getFullName())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "fullName"));
            }
            userService.updateProfile(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/requestAddMoney")
    public AppResponse requestAddMoney(@RequestBody AddMoneyRequest request) {
        try {
            if (StringUtils.isEmpty(request.getInformation())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "information"));
            }
            if (StringUtils.isEmpty(request.getAmount())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "amount"));
            }
            userService.addRequestAddMoney(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }
}
