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

    // WORKING?
    // post TX
    @GetMapping(value = "/test1")
    public RESTresponse test1() {
        System.out.println("Entered Rest: test1");
        String response = "";

        // send 100 coins to address (1,0)
        uint128 zero = new uint128(0, 0);
        uint128 address1 = new uint128(1, 0);
        Response_status status = send_transferCoins(zero, address1, 100L);
        response += "\nsubmit transaction response: " + status.getResponse();
        System.out.println("\n\n\n\n\n\n\ngot status " + status.getResponse());

        // get utxos of address (1,0)
        List<UTxO> address1_utxos = send_getUtxos(address1, 0);
        assert address1_utxos != null;
        System.out.println("\n\n\n\n\n\n\ngot utxo List for address1 ["
                + address1_utxos.stream().map(UTxO::toString).collect(Collectors.joining(",\n")) + "]");
        response += "\nsend_getUtxos response: " + address1_utxos.toString();
        assert address1_utxos.size() == 1;

        // send 10 coins to address (1,1)
        uint128 address2 = new uint128(1, 1);
        TR receiver = new TR(address2, 10);
        TR remain = new TR(zero, 90);
        List<TR> output = new ArrayList<TR>();
        output.add(remain);
        output.add(receiver);
        TX transaction = new TX(address1_utxos, output);
        System.out.println("[test1] sent " + transaction);
        status = send_submitTransaction(transaction);
        response += "\nsubmit transaction response: " + status.getResponse();
        System.out.println("\n\n\n\n\n\n\ngot status " + status.getResponse());
        List<TX> address1_transactions = send_getTransactions(address1, 0);
        assert address1_transactions != null;
        System.out.println("\n\n\n\n\n\n\ngot transaction List for address1 " + address1_transactions.toString());
        response += "\nsend_getTransactions response: " + address1_transactions.toString();
        List<TX> address2_transactions = send_getTransactions(address2, 0);
        assert address2_transactions != null;
        System.out.println("\n\n\n\n\n\n\ngot transaction List for address1 " + address2_transactions.toString());
        response += "\nsend_getTransactions response: " + address2_transactions.toString();

        address1_utxos = send_getUtxos(address1, 0);
        assert address1_utxos != null;
        System.out.println("\n\n\n\n\n\n\ngot utxo List for address1 ["
                + address1_utxos.stream().map(UTxO::toString).collect(Collectors.joining(",\n")) + "]");
        response += "\nsend_getUtxos response: " + address2_transactions.toString();

        return new RESTresponse("SUCCESS", response);
    }

    @GetMapping(value = "/test2")
    public RESTresponse test2() {
        System.out.println("Entered Rest: test2");
        uint128 zero = new uint128(0, 0);

        uint128 address1 = zero;
        uint128 address2 = new uint128(0, 2);
        uint128 address3 = new uint128(0, 2);
        UTxO uTxO = new UTxO(zero, zero);
        List<UTxO> input = new ArrayList<UTxO>();
        input.add(uTxO);
        TR receiver = new TR(address2, 2);
        TR remain = new TR(address1, -3);
        List<TR> output = new ArrayList<TR>();
        output.add(remain);
        output.add(receiver);
        TX transaction = new TX(input, output);
        System.out.println("[test1] sent " + transaction);
        Response_status status = send_submitTransaction(transaction);
        String response = "submit transaction response: " + status.getResponse();
        System.out.println("\n\n\n\n\n\n\ngot status " + status.getResponse());
        List<TX> address1_transactions = send_getTransactions(address1, 0);
        assert address1_transactions != null;
        System.out.println("\n\n\n\n\n\n\ngot transaction List for address1 " + address1_transactions.toString());
        response += "\nsend_getTransactions response: " + address1_transactions.toString();
        List<TX> address2_transactions = send_getTransactions(address2, 0);
        assert address2_transactions != null;
        System.out.println("\n\n\n\n\n\n\ngot transaction List for address2 " + address2_transactions.toString());
        response += "\nsend_getTransactions response: " + address2_transactions.toString();
        return new RESTresponse("SUCCESS", response);
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
    @GetMapping(value = "/SendMoney/{address}/{amount}")
    public RESTresponse SendMoney(@RequestBody string SendMoney, @PathVariable long address,
            @PathVariable long amount) {
        System.out.println("Entered Rest: SendMoney");
        // DO THINGS

        //
        return new RESTresponse("SUCCESS", "SendMoney OK");
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

    // Get /utxo/{address} */
    @GetMapping(value = "/utxo/{adress}")
    public RESTresponse get_utxo(@PathVariable long adress) {
        UTxO uTxO = new UTxO(new uint128(10, 10), adress);
        Gson gson = new Gson();
        String json = gson.toJson(uTxO);
        System.out.println(json);
        TX newtx = gson.fromJson(json, UTxO.class);
        System.out.println(newtx.toString());

        RESTresponse result = new RESTresponse("SUCCESS", json);
        return result;
    }

    // Send Coinds */
    @GetMapping(value = "/SendCoins")
    public RESTresponse set_coins() {
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

    @RequestMapping(value = "/SendCoind/{amount}", method = RequestMethod.GET)
    public RestResponse set_coins(@PathVariable long amount) {
        RestServer server = new RestServer();
        Tx tx = tx.getStatus(state);
        RESTresponse result = new RESTresponse("SUCCESS", json);
        return result;
    }

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
 * 2. post /sendcoins - body=sender&reseve&amount
 * 3. get /utxo/{adress}
 * 4. get /transaction?amount={amount}
 * 5. get /transaction/{adress}?amount={amount}
 * 4+5 can be the same function.
 * function: send_getTransactions(adress, amount) - checks if exist, if
 * NULL=adress it does 4 else its 5.
 * if amount is 0 than its like it got no amount.
 * 
 */