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
import tch1904.mwd.controllers.request.*;
import tch1904.mwd.entity.dto.UserDTO;
import tch1904.mwd.services.CommonServices;
import tch1904.mwd.services.ProductServices;
import tch1904.mwd.services.RequestServices;
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

    @Autowired
    private ProductServices productServices;

    @Autowired
    private RequestServices requestServices;

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

    @PostMapping("/changePassword")
    public AppResponse changePassword(@RequestBody ChangePassRequest request) {
        try {
            if (StringUtils.isEmpty(request.getOldPass())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Old Password"));
            }
            if (StringUtils.isEmpty(request.getNewPass())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "New Password"));
            }
            userService.changePasswordAuth(request);
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

    @PostMapping("/requestBeSinger")
    public AppResponse requestBeSinger(@RequestBody RegisterSingerRequest request) {
        try {
            if (StringUtils.isEmpty(request.getLinkFile())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "information"));
            }
            userService.requestBeSinger(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/findRequestBeSinger")
    public AppResponse findRequestBeSinger(@RequestBody SearchRequestListRequest request) {
        try {
            return new AppResponseSuccess(requestServices.findRequestBeSinger(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/setUserAvatar")
    public AppResponse setUserAvatar(@RequestBody SimpleStringRequest request) {
        try {
            if (StringUtils.isEmpty(request.getString())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Avatar url"));
            }
            userService.setUserAvatar(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/findRequestAddMoney")
    public AppResponse findRequestAddMoney(@RequestBody SearchRequestListRequest request) {
        try {
            return new AppResponseSuccess(requestServices.findRequestAddMoney(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/requestPublishProduct")
    public AppResponse requestPublishProduct(@RequestBody PublishProductRequest request) {
        try {
            if (StringUtils.isEmpty(request.getProductId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "productId"));
            }
            if (StringUtils.isEmpty(request.getPrice())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Price"));
            }
            userService.requestPublishProduct(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/findRequestPublishProduct")
    public AppResponse findRequestPublishProduct(@RequestBody SearchRequestListRequest request) {
        try {
            return new AppResponseSuccess(requestServices.findRequestPublishProduct(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/addView")
    public AppResponse addView(@RequestBody SimpleStringRequest request) {
        try {
            productServices.addView(Integer.parseInt(request.getString()));
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/addProduct")
    public AppResponse addProduct(@RequestBody AddProductRequest request) {
        try {
            productServices.addProduct(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/markProduct")
    public AppResponse markProduct(@RequestBody MarkProductRequest request) {
        try {
            productServices.markProduct(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/searchProduct")
    public AppResponse searchProduct(@RequestBody SearchProductRequest request) {
        try {
            return new AppResponseSuccess(productServices.searchProduct(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/getListOwnProduct")
    public AppResponse getListOwnProduct() {
        try {
            return new AppResponseSuccess(productServices.getListOwnProduct());
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/buyProduct")
    public AppResponse buyProduct(@RequestBody SimpleStringRequest request) {
        try {
            productServices.buyProduct(Integer.parseInt(request.getString()));
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/createPlayList")
    public AppResponse createPlayList(@RequestBody CreatePlayListRequest request) {
        try {
            productServices.createPlayList(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/getAllPlayListsByUsername")
    public AppResponse getAllPlayListsByUsername(@RequestBody SimpleStringRequest request) {
        try {
            return new AppResponseSuccess(productServices.getAllPlayListsByUsername(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/getPlayListItem")
    public AppResponse getPlayListItem(@RequestBody SimpleStringRequest request) {
        try {
            if (StringUtils.isEmpty(request.getString())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "listId"));
            }
            return new AppResponseSuccess(productServices.getPlayListItem(Integer.parseInt(request.getString())));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/addToPlayList")
    public AppResponse addToPlayList(@RequestBody AddOrDeleteItemOfPlayListRequest request) {
        try {
            productServices.addToPlayList(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/removeFromPlayList")
    public AppResponse removeFromPlayList(@RequestBody AddOrDeleteItemOfPlayListRequest request) {
        try {
            productServices.removeFromPlayList(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/addComment")
    public AppResponse addComment(@RequestBody AddCommentsRequest request) {
        try {
            userService.addComments(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/deleteComment")
    public AppResponse deleteComment(@RequestBody SimpleStringRequest request) {
        try {
            userService.deleteComment(Integer.parseInt(request.getString()));
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/loadAllComments")
    public AppResponse loadAllComments(@RequestBody SearchCommentRequest request) {
        try {
            return new AppResponseSuccess(userService.loadAllComments(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }
}
