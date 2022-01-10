package CryptoSystem.SystemServer.Spring;

import CryptoSystem.gRPCwrapper.SystemServerImpl;
import CryptoSystem.types.TX;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import notsystemserver.grpc.Response_status;
import notsystemserver.grpc.SystemServerGrpc;
import notsystemserver.grpc.TX_m;
import org.springframework.web.bind.annotation.*;

@RestController
public class RESTcontroller {

    @PostMapping(value = "/TX")
    public RESTresponse TX(@RequestBody TX tx_request) {
        System.out.println("Entered Rest: init");
        SystemServerImpl grpc_server = new SystemServerImpl();
        Integer port = grpc_server.getGrpcPort();


        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();

        SystemServerGrpc.SystemServerBlockingStub stub = SystemServerGrpc.newBlockingStub(channel);


        TX_m tx_m = tx_request.to_tx_m();

        Response_status res_grpc = stub.submitTransaction(tx_m);

        RESTresponse res;

        res =new RESTresponse(res_grpc.getResponse());

        channel.shutdown();
        return res;
    }

    @GetMapping(value = "/init")
    public RESTresponse init() {
        System.out.println("Entered Rest: init");
        RESTresponse res;
        if (SystemServerImpl.send_initServer())
            res = new RESTresponse("Init OK");
        else
            res = new RESTresponse("Init Failed");
        return res;
    }
}
