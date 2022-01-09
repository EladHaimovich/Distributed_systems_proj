package CryptoSystem.SystemServer.Spring;

public class RESTresponse {
    private final String responseMessage;

    RESTresponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
