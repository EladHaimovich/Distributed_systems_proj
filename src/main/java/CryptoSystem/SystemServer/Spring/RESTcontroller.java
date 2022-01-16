package CryptoSystem.SystemServer.Spring;

import CryptoSystem.gRPCwrapper.SystemServerImpl;
import CryptoSystem.types.TR;
import CryptoSystem.types.TX;
import CryptoSystem.types.UTxO;
import CryptoSystem.types.uint128;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import notsystemserver.grpc.Response_status;
import notsystemserver.grpc.SystemServerGrpc;
import notsystemserver.grpc.TX_m;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
public class RESTcontroller {

    // WORKING?
    // post TX
    @PostMapping(value = "/TX")
    public RESTresponse TX(@RequestBody TX tx_request) {
        System.out.println("Entered Rest: init");
        SystemServerImpl grpc_server = new SystemServerImpl();
        Integer port = SystemServerImpl.getGrpcPort();

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();

        SystemServerGrpc.SystemServerBlockingStub stub = SystemServerGrpc.newBlockingStub(channel);

        TX_m tx_m = tx_request.to_grpc();

        Response_status res_grpc = stub.submitTransaction(tx_m);

        RESTresponse res;

        res = new RESTresponse(res_grpc.getResponse());

        channel.shutdown();
        return res;
    }

    // GOOD !!
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
    /* Submit == Post , anything else is a "get". */

    // Submit money to address
    @GetMapping(value = "/SendMoney")
    public RESTresponse SendMoney() {
        System.out.println("Entered Rest: SendMoney");
        // DO THINGS

        //
        return new RESTresponse("SendMoney OK");
    }
    // Submit transaction list

    // get unspent UTXO

    // get tarnsaction history (for Address? or for all?)
    // need to support a limit for the number of transactions.
    @GetMapping(value = "/TX_HISTORY")
    public RESTresponse TX_HISTORY() {
        System.out.println("Entered Rest: TX_HISTORY");
        // DO THINGS

        //
        return new RESTresponse("TX_HISTORY OK");
    }

    /* example */
    @GetMapping(value = "/TX_FORMAT")
    public TX get_tx_format() {
        // long timestamp, List<UTxO> utxos, List<TR> trs
        uint128 tx_id = new uint128(0, 50);
        long timestamp = 8128;
        List<UTxO> utxos = new ArrayList<UTxO>();
        List<TR> trs = new ArrayList<TR>();
        TX example = new TX(tx_id, timestamp, utxos, trs);
        return example;
    }

}
