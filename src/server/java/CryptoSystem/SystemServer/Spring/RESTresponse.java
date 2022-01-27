package CryptoSystem.SystemServer.Spring;

public class RESTresponse {
    private final String status;
    private final String responseMessage;

    RESTresponse(String status, String responseMessage) {
        this.status = status;
        this.responseMessage = responseMessage;
    }

    public String getStatus() {
        return status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
