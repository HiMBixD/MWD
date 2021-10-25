package tch1904.mwd.constant.components.response;


import tch1904.mwd.constant.AppConstants;
import tch1904.mwd.constant.components.Message;

public class AppResponseSuccess extends AppResponse{
    public AppResponseSuccess(){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
    }
    public AppResponseSuccess(Object data){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
        this.setData(data);
    }
    public AppResponseSuccess(Message message){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
        this.setResponseMessage(message);
    }
    public AppResponseSuccess(Object data, Message message){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
        this.setData(data);
        this.setResponseMessage(message);
    }

}
