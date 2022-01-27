package CryptoSystem.SystemServer.Spring;

public class RESTresponse {
    private final String success;
    private final String responseMessage;

    RESTresponse(String success, String responseMessage) {
        this.success = success;
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
