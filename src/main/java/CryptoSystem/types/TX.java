package CryptoSystem.types;

import com.google.protobuf.ByteString;
import notsystemserver.grpc.TR_m;
import notsystemserver.grpc.TX_m;
import notsystemserver.grpc.UTxO_m;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TX {

//    bytes Tx_id = 1;
//    bytes sender = 2;
//    bytes receiver = 3;
//    uint64  amount = 4;
//    uint64 timestamp = 5;
//    repeated UTxO_m utxos = 6;
//    repeated TR_m trs = 7;

    byte[] sender;
    byte[] receiver;
    long amount;
    List<UTxO> utxos = null;
    byte[] tx_id = null;
    long timestamp = 0;
    List<TR> trs = null;




    public TX(byte[] sender, byte[] receiver, long amount, List<UTxO> utxos, byte[] tx_id, List<TR> trs, long timestamp) {
        this.sender = sender.clone();
        this.receiver = receiver.clone();
        this.amount = amount;
        this.utxos = new ArrayList<UTxO>(utxos);
        this.tx_id = tx_id.clone();
        this.trs = new ArrayList<>(trs);
        this.timestamp = timestamp;
    }

    public TX(byte[] sender, byte[] receiver, long amount, List<UTxO> utxos) {
        this(sender, receiver,amount,utxos, null, null, 0);
    }

    public TX(byte[] sender, byte[] receiver, long amount) {
        this(sender, receiver,amount, null);
    }

    public TX(TX tx) {
        this(tx.getSender(),tx.getReceiver(),tx.getAmount(),tx.getUtxos(),tx.getTx_id(),tx.getTrs(),tx.getTimestamp());
    }

    public TX(TX_m tx_m) {
        tx_id = tx_m.getTxId().toByteArray();
        sender = tx_m.getSender().toByteArray();
        receiver = tx_m.getReceiver().toByteArray();
        amount = tx_m.getAmount();
        timestamp = tx_m.getTimestamp();

        if(tx_m.getUtxosCount() > 0)
            utxos = tx_m.getUtxosList().stream().map(UTxO::new).collect(Collectors.toList());
        else
            utxos = null;

        if(tx_m.getTrsCount() > 0)
            trs = tx_m.getTrsList().stream().map(TR::new).collect(Collectors.toList());
        else
            utxos = null;
    }

    public TX_m to_tx_m() {
        List<UTxO_m> uTxO_ms = utxos.stream().map(UTxO::to_utxo_m).collect(Collectors.toList());
        List<TR_m> tr_ms = trs.stream().map(TR::to_tr_m).collect(Collectors.toList());
        return TX_m.newBuilder()
                    .setTxId(ByteString.copyFrom(tx_id))
                    .setSender(ByteString.copyFrom(sender))
                    .setReceiver(ByteString.copyFrom(receiver))
                    .setAmount(amount).setTimestamp(timestamp)
                    .addAllUtxos(uTxO_ms)
                    .addAllTrs(tr_ms)
                    .build();
    }

    public void addUtxoList(List<UTxO> utxos) {
        assert (this.utxos == null);
        assert (timestamp == 0);
        this.utxos = new ArrayList<UTxO>(utxos);
    }


    public boolean process(byte[] tx_id, long timestamp) {
        assert (this.utxos != null);
        assert (timestamp == 0);

        long sum = 0;
        for (UTxO utxo : this.utxos) {
            if (sender.equals(utxo.getAddress()))
                return false;
            sum += utxo.getAmount();
        }
        if (Long.compareUnsigned(sum, amount) < 0)
            return false;

        this.timestamp = timestamp;
        this.tx_id = tx_id.clone();
        trs = new ArrayList<TR>();
        trs.add(new TR(receiver, amount));
        if (Long.compareUnsigned(sum, amount) > 0)
            trs.add(new TR(sender, amount));
        return true;
    }

    public boolean validate(long amount) {
        long sum = 0;
        for (UTxO utxo : this.utxos) {
            sum += utxo.getAmount();
        }
        if (Long.compareUnsigned(sum, amount) < 0)
            return false;
        return true;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isProcessed() {
        return timestamp != 0;
    }

    public List<UTxO> getUtxos() {
        return new ArrayList<UTxO>(utxos);
    }

    public long get_amount_by_address(byte[] address) {
        for (TR tr: trs) {
            if (tr.getAddress().equals(address))
                return tr.getAmount();
        }
        return 0;
    }

    public byte[] getTx_id() {
        return tx_id.clone();
    }

    public List<TR> getTrs() {
        return new ArrayList<>(trs);
    }

    public long getAmount() {
        return amount;
    }

    public byte[] getReceiver() {
        return receiver.clone();
    }

    public byte[] getSender() {
        return sender.clone();
    }
}
