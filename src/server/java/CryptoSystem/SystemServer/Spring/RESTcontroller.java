package CryptoSystem.SystemServer.Spring;

import CryptoSystem.gRPCwrapper.SystemServerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import CryptoSystem.types.*;
import notsystemserver.grpc.Response_status;
import notsystemserver.grpc.Send_Coins_req;

import org.springframework.web.bind.annotation.*;

import static CryptoSystem.gRPCwrapper.SystemServerImpl.*;
import com.google.gson.Gson;

@RestController
public class RESTcontroller {

    /* format examples */
    @GetMapping(value = "/SendMoney/format")
    public RESTresponse SendMoney_format() {
        System.out.println("Entered Rest: SendMoney_format");
        SendCoinsArgs args = new SendCoinsArgs();
        Gson gson = new Gson();
        String json = gson.toJson(args);
        return new RESTresponse("SUCCESS", json);
    }

    @GetMapping(value = "/transaction/format")
    public RESTresponse transaction_format() {
        uint128 tx_id = new uint128(0, 50);
        long timestamp = 8128;
        UTxO uTxO = new UTxO(new uint128(10, 10), new uint128(15, 15));
        List<UTxO> utxos = new ArrayList<UTxO>();
        utxos.add(uTxO);
        List<TR> trs = new ArrayList<TR>();
        TR tr = new TR(new uint128(10, 15), 100);
        trs.add(tr);
        TX tx = new TX(tx_id, timestamp, utxos, trs);

        Gson gson = new Gson();
        String json = gson.toJson(tx);
        System.out.println(json);
        TX newtx = gson.fromJson(json, TX.class);
        System.out.println(newtx.toString());

        RESTresponse result = new RESTresponse("SUCCESS", json);
        return result;
    }

    @GetMapping(value = "/get_args/format")
    public RESTresponse address_format() {
        System.out.println("Entered Rest: address_format");
        GetArgs args = new GetArgs(new uint128(0,0),0);
        Gson gson = new Gson();
        String json = gson.toJson(args);
        return new RESTresponse("SUCCESS", json);
    }

    @GetMapping(value = "/address/format/null")
    public RESTresponse address_format_null() {
        System.out.println("Entered Rest: address_format_null");
        GetArgs args = new GetArgs(null,0);
        Gson gson = new Gson();
        String json = gson.toJson(args);
        return new RESTresponse("SUCCESS", json);
    }

    // GOOD !!
    @GetMapping(value = "/init")
    public RESTresponse init() {
        System.out.println("Entered Rest: init");
        RESTresponse res;
        if (SystemServerImpl.send_initServer())
            res = new RESTresponse("SUCCESS", "Init OK");
        else
            res = new RESTresponse("fail", "Init Failed");
        return res;
    }
    /* Submit == Post , anything else is a "get". */

    // transfer coins to address
    @PostMapping(value = "/transaction")
    public RESTresponse submit_transaction(@RequestBody String json) {
        System.out.println("Entered Rest: SendCoins");
        Gson gson = new Gson();
        TX args = gson.fromJson(json, TX.class);
        TX transaction = new TX(args.getUtxos(), args.getTrs());

        Response_status status = SystemServerImpl.send_submitTransaction(transaction);

        return new RESTresponse(status.getSuccess()? "Success":"Failure", status.getResponse());
    }

    // transfer coins to address
    @PostMapping(value = "/SendCoins")
    public RESTresponse SendCoins(@RequestBody String json) {
        System.out.println("Entered Rest: SendCoins");
        Gson gson = new Gson();
        SendCoinsArgs args = gson.fromJson(json, SendCoinsArgs.class);

        Response_status status = SystemServerImpl.send_transferCoins(args.getSender(), args.getReceiver(), args.getAmount());

        return new RESTresponse(status.getSuccess()? "Success":"Failure", status.getResponse());
    }

    // get unspent UTXO
    @GetMapping(value = "/utxo")
    public RESTresponse get_utxos(@RequestBody String args) {
        Gson gson = new Gson();
        GetArgs parsed_args = gson.fromJson(args, GetArgs.class);
        List<UTxO> utxos = SystemServerImpl.send_getUtxos(parsed_args.getAddress(),parsed_args.getAmount());

        String json = gson.toJson(utxos);

        RESTresponse result = new RESTresponse("SUCCESS", json);
        return result;
    }



    // get tarnsaction history (for Address? or for all?)
    // need to support a limit for the number of transactions.
    @GetMapping(value = "/TX_HISTORY")
    public RESTresponse TX_HISTORY(@RequestBody String args) {
        System.out.println("Entered Rest: TX_HISTORY");
        Gson gson = new Gson();
        GetArgs parsed_args = gson.fromJson(args, GetArgs.class);
        List<TX> transactions = SystemServerImpl.send_getTransactions(parsed_args.getAddress(),parsed_args.getAmount());
        String json = gson.toJson(transactions);
        RESTresponse result = new RESTresponse("SUCCESS", json);
        return result;
    }




}
