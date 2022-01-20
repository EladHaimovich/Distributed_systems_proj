package CryptoSystem.types;

import com.google.protobuf.ByteString;
import notsystemserver.grpc.TR_m;
import notsystemserver.grpc.TX_m;
import notsystemserver.grpc.UTxO_m;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TX {

    uint128 tx_id;
    long timestamp = 0;
    List<UTxO> utxos = null;
    List<TR> trs = null;

    public TX(uint128 tx_id, long timestamp, List<UTxO> utxos, List<TR> trs) {
        if (tx_id != null)
            this.tx_id = tx_id.clone();
        this.timestamp = timestamp;
        this.utxos = new ArrayList<>(utxos);
        this.trs = new ArrayList<>(trs);
    }

    public TX(uint128 tx_id, List<UTxO> utxos, List<TR> trs) {
        this(tx_id, 0, utxos, trs);
    }

    public TX(List<UTxO> utxos, List<TR> trs) {
        this(null, 0, utxos, trs);
    }

    public TX(TX_m from) {
        this.tx_id = new uint128(from.getTxId());
        this.timestamp = from.getTimestamp();
        this.utxos = from.getUtxosList().stream().map(UTxO::new).collect(Collectors.toList());
        this.trs = from.getTrsList().stream().map(TR::new).collect(Collectors.toList());
    }

    public TX_m to_grpc() {
        TX_m res = TX_m.newBuilder()
                .setTimestamp(timestamp)
                .setTxId(tx_id.to_grpc())
                .addAllUtxos(utxos.stream().map(UTxO::to_grpc).collect(Collectors.toList()))
                .addAllTrs(trs.stream().map(TR::to_grpc).collect(Collectors.toList()))
                .build();
        return res;
    }

    public boolean assign_TXid(uint128 tx_id) {
        if (this.tx_id != null)
            return false;
        this.tx_id = tx_id;
        return true;
    }

    public boolean assign_timestamp(long timestamp) {
        if (this.timestamp != 0)
            return false;
        this.timestamp = timestamp;
        return true;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<UTxO> getUtxos() {
        return utxos;
    }

    public List<TR> getTrs() {
        return trs;
    }

    public uint128 getTx_id() {
        return tx_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TX)) return false;
        TX tx = (TX) o;
        return getTx_id().equals(tx.getTx_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTx_id());
    }

    public static TX gen_TX() {
        uint128 zero = new uint128(0,0);
        TR gen_tr = new TR(zero, -1);
        List<TR> tr_list = new ArrayList<TR>();
        tr_list.add(gen_tr);
        return new TX(zero, 0,new ArrayList<>(), tr_list);
    }

    @Override
    public String toString() {
        return "TX{" +
                "tx_id=" + tx_id +
                ", timestamp=" + timestamp +
                ", utxos=" + utxos +
                ", trs=" + trs +
                '}';
    }
}
