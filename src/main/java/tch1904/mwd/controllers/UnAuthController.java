package tch1904.mwd.controllers;

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
import tch1904.mwd.repository.UserRepository;
import tch1904.mwd.services.CommonServices;
import tch1904.mwd.services.ProductServices;
import tch1904.mwd.services.RequestServices;
import tch1904.mwd.services.UserService;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/unau/")
public class UnAuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommonServices commonServices;
    @Autowired
    private ProductServices productServices;
    @Autowired
    private RequestServices requestServices;

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

    @PostMapping("/getRecommendSongs")
    public AppResponse getRecommendSongs() {
        try {
            return new AppResponseSuccess(productServices.getRecommendSongs());
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }


    @PostMapping("/getProductInfo")
    public AppResponse getProductInfo(@RequestBody SimpleStringRequest request) {
        try {
            if (StringUtils.isEmpty(request.getString())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "fileId"));
            }
            return new AppResponseSuccess(productServices.getProductInfo(request.getString()));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
        }
    }

    @PostMapping("/addViewed")
    public AppResponse addViewed(@RequestBody SimpleStringRequest request) {
        try {
            if (StringUtils.isEmpty(request.getString())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "productId"));
            }
            productServices.addViewed(Integer.parseInt(request.getString()));
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
