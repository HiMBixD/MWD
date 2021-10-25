package tch1904.mwd.constant.components.response;


import tch1904.mwd.constant.AppConstants;
import tch1904.mwd.constant.components.Message;

public class AppResponseFailure extends AppResponse{
    public AppResponseFailure(){
        this.setSuccess(AppConstants.RESULT_ERROR);
    }
    public AppResponseFailure(Message mess){
        this.setSuccess(AppConstants.RESULT_ERROR);
        this.setResponseMessage(mess);
    }
    public AppResponseFailure(String mess){
        this.setSuccess(AppConstants.RESULT_ERROR);
        this.setResponseMessage(new Message(mess));
    }
}
