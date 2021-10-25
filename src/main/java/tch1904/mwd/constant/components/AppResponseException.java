package tch1904.mwd.constant.components;

public class AppResponseException extends RuntimeException {
    public Message responseMessage;
    public AppResponseException(Message responseMessage) {
        super(responseMessage.getMessage()+ " " + responseMessage.getErrorCode());
        this.responseMessage = responseMessage;
    }

}
