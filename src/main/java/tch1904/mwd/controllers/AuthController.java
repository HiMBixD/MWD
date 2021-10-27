package tch1904.mwd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tch1904.mwd.authen.JwtProvider;
import tch1904.mwd.constant.AppConstants;
import tch1904.mwd.constant.components.AppResponseException;
import tch1904.mwd.constant.components.Message;
import tch1904.mwd.constant.components.response.AppResponse;
import tch1904.mwd.constant.components.response.AppResponseFailure;
import tch1904.mwd.constant.components.response.AppResponseSuccess;
import tch1904.mwd.controllers.request.ChangePasswordRequest;
import tch1904.mwd.controllers.request.CheckUsernameRequest;
import tch1904.mwd.controllers.request.LoginRequest;
import tch1904.mwd.controllers.request.RegisterRequest;
import tch1904.mwd.entity.User;
import tch1904.mwd.entity.dto.UserDTO;
import tch1904.mwd.repository.UserRepository;
import tch1904.mwd.services.CommonServices;
import tch1904.mwd.services.UserService;

import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin
@RequestMapping("/unau/")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CommonServices commonServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth")
    public AppResponse auth(@RequestBody LoginRequest request) {
        Message message = new Message();
        try {
            UserDTO userDTO = userService.findByUsernameAndPassword(request.getUsername(), request.getPassword());
            if (null == userDTO) {
                message.setMessage(AppConstants.NOT_FOUND);
                message.setErrorCode("wrongIDorPassword");
            }
            assert userDTO != null;
            String token = jwtProvider.generateToken(userDTO.getUserName());
            return new AppResponseSuccess(token);
        } catch (Exception e) {
            return new AppResponseFailure(message);
        }
    }

    @PostMapping("/checkDupUsername")
    public AppResponse auth(@RequestBody CheckUsernameRequest request) {
        try {
            if (StringUtils.isEmpty(request.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "username"));
            }
            Optional<User> user = userService.findByUsername(request.getUsername());
            if (user.isPresent()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "username"));
            }
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/register")
    public AppResponse auth(@RequestBody RegisterRequest request) {
        try {
            UserDTO userDTO = userService.createUser(request);
            assert userDTO != null;
            return new AppResponseSuccess(userDTO);
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/sendOtp")
    public AppResponse sendOtp(@RequestBody CheckUsernameRequest request) {
        try {
            UserDTO userDTO = userService.getUserInfo(request.getUsername());
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

    @PostMapping("/changePassword")
    public AppResponse changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            if (StringUtils.isEmpty(request.getOtp())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Otp"));
            }
            if (StringUtils.isEmpty(request.getPassword())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Password"));
            }
            if (StringUtils.isEmpty(request.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Username"));
            }
            // send otp => type otp + pass + username => save new pass
            userService.changePassword(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

//    @PostMapping("/reset-password")
//    public AppResponse resetPassword(@RequestBody ResetPasswordRequest request) {
//        try {
//            if (StringUtils.isEmpty(request.getUsername())) {
//                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "username"));
//            }
//            if (StringUtils.isEmpty(request.getEmail())) {
//                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "email"));
//            }
//            Optional<User> optionalUser = userService.findByUsername(request.getUsername());
//            if (optionalUser.isEmpty()) {
//                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "username"));
//            }
//            if (!request.getEmail().equals(optionalUser.get().getEmail())) {
//                throw new AppResponseException(new Message(AppConstants.INVALID, "email"));
//            }
//            String newPass = randomString(6);
//            User user = optionalUser.get();
//            user.setPassword(passwordEncoder.encode(newPass));
//            userRepository.save(user);
////            userService.resetPass(newPass, optionalUser.get().getUsername());
//            String mailContent = "<p>Dear Mr/Mrs <strong>" + optionalUser.get().getFirstName() + "</strong>,</p>" +
//                    "<p>Your password has been reset, your new password is: <strong>" + newPass + "</strong></p>" +
//                    "<p>Thank you for using our service,</p>" +
//                    "<p>Please login to WED web to confirm your account information</p>" +
//                    "<p>Best regards.</p>";
//            String mailHeader = "Reset new Password for WED system login";
//            commonServices.sendEmail(optionalUser.get().getEmail(), mailContent, mailHeader);
//            return new AppResponseSuccess();
//        } catch (AppResponseException exception) {
//            return new AppResponseFailure(exception.responseMessage);
//        } catch (Exception e) {
//            return new AppResponseFailure(e.getMessage());
//        }
//    }
}
