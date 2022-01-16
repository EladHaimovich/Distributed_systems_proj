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
        this.tx_id = tx_id.clone();
        this.timestamp = timestamp;
        this.utxos = new ArrayList<>(utxos);
        this.trs = new ArrayList<>(trs);
    }

    public TX(uint128 tx_id, List<UTxO> utxos, List<TR> trs) {
        this(tx_id, 0, utxos, trs);
    }

    public TX(TX_m from) {
        this.tx_id = new uint128(from.getTxId());
        this.timestamp = from.getTimestamp();
        this.utxos = from.getUtxosList().stream().map(UTxO::new).collect(Collectors.toList());
        this.trs = from.getTrsList().stream().map(TR::new).collect(Collectors.toList());
    }

    public boolean assign_timestamp(long timestamp) {
        if (this.timestamp != 0)
            return false;
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
}
