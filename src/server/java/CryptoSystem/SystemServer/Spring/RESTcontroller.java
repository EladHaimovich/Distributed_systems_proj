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
    @GetMapping(value = "/SendMoney/example")
    public RESTresponse SendMoney_example() {
        System.out.println("Entered Rest: SendMoney_example");
        SendCoinsArgs args = new SendCoinsArgs();
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




    // Submit money to address
    @GetMapping(value = "/SendMoney")
    public RESTresponse SendMoney(@RequestBody String json) {
        System.out.println("Entered Rest: SendMoney");
        Gson gson = new Gson();
        SendCoinsArgs args = gson.fromJson(json, SendCoinsArgs.class);

        Response_status status = SystemServerImpl.send_transferCoins(args.getSender(), args.getReceiver(), args.getAmount());

        return new RESTresponse(status.getSuccess()? "Success":"Failure", "SendMoney OK");
    }

    // get unspent UTXO

    // get tarnsaction history (for Address? or for all?)
    // need to support a limit for the number of transactions.
    @GetMapping(value = "/TX_HISTORY")
    public RESTresponse TX_HISTORY() {
        System.out.println("Entered Rest: TX_HISTORY");
        // DO THINGS

        //
        return new RESTresponse("SUCCESS", "TX_HISTORY OK");
    }

    // Get /transaction */
    @GetMapping(value = "/TX_FORMAT")
    public RESTresponse get_tx_format() {
        // long timestamp, List<UTxO> utxos, List<TR> trs
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
        // return example;
    }

//    // Get /utxo/{address} */
//    @GetMapping(value = "/utxo/{adress}")
//    public RESTresponse get_utxo(@PathVariable long adress) {
//        UTxO uTxO = new UTxO(new uint128(10, 10), adress);
//        Gson gson = new Gson();
//        String json = gson.toJson(uTxO);
//        System.out.println(json);
//        TX newtx = gson.fromJson(json, UTxO.class);
//        System.out.println(newtx.toString());
//
//        RESTresponse result = new RESTresponse("SUCCESS", json);
//        return result;
//    }
//
//    // Send Coinds */
//    @GetMapping(value = "/SendCoins")
//    public RESTresponse set_coins() {
//
//        uint128 tx_id = new uint128(0, 50);
//        long timestamp = 8128;
//        UTxO uTxO = new UTxO(new uint128(10, 10), new uint128(15, 15));
//        List<UTxO> utxos = new ArrayList<UTxO>();
//        utxos.add(uTxO);
//        List<TR> trs = new ArrayList<TR>();
//        TR tr = new TR(new uint128(10, 15), 100);
//        trs.add(tr);
//        TX tx = new TX(tx_id, timestamp, utxos, trs);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(tx);
//        System.out.println(json);
//        TX newtx = gson.fromJson(json, TX.class);
//        System.out.println(newtx.toString());
//
//        RESTresponse result = new RESTresponse("SUCCESS", json);
//        return result;
//        // return example;
//    }
//
//    @RequestMapping(value = "/SendCoind/{amount}", method = RequestMethod.GET)
//    public RESTresponse send_coins(@PathVariable long amount) {
//
//        TX tx = tx.getStatus(state);
//        RESTresponse result = new RESTresponse("SUCCESS", json);
//        return result;
//    }

    // // Post /transaction */
    // @PostMapping(value = "/transaction/{id}/{timestamp}/{utxos}/{trs}")
    // public RESTresponse post_tx(@RequestBody TX newTx, @PathVariable uint128 id,
    // @PathVariable Long timestamp) {
    // // long timestamp, List<UTxO> utxos, List<TR> trs
    // UTxO uTxO = new UTxO(id, timestamp);
    // List<UTxO> utxos = new ArrayList<UTxO>();
    // utxos.add(uTxO);
    // Gson gson = new Gson();
    // String json = gson.toJson(tx);
    // TX tx = gson.fromJson(json, TX.class);
    // System.out.println(tx.toString());
    // return new RESTresponse("SUCCESS", null);
    // // return example;
    // }

}

// // post /transaction */
// @PostMapping(value = "/transaction/{id}/{timestamp}/{utxos}/{trs}")
// public RESTresponse post_tx(@RequestBody TX newTx, @PathVariable uint128 id,
// @PathVariable Long timestamp,
// @PathVariable List<UTxO> utxos, @PathVariable List<TR> trs) {
// return new TX(id, timestamp, utxos, trs); // can't return TX...

/*
 * Sababa
 * 1. post /transaction - body=tx
 * 2. post /sendcoins - body=sender&receiver&amount
 * 3. get /utxo/{adress}
 * 4. get /transaction?amount={amount}
 * 5. get /transaction/{adress}?amount={amount}
 * 4+5 can be the same function.
 * function: send_getTransactions(adress, amount) - checks if exist, if
 * NULL=adress it does 4 else its 5.
 * if amount is 0 than its like it got no amount.
 * 
 */