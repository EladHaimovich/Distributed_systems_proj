package CryptoSystem.types;

import com.google.protobuf.ByteString;
import notsystemserver.grpc.*;

import java.util.Arrays;
import java.util.Objects;

public class UTxO {

    uint128 tx_id;
    uint128 address;

    public UTxO(uint128 tx_id, uint128 address) {
        this.tx_id = tx_id.clone();
        this.address = address.clone();
    }

    public UTxO(UTxO from) {
        this(from.tx_id, from.address);
    }

    public UTxO(UTxO_m utxo_m) {
        tx_id = new uint128(utxo_m.getTxId());
        address = new uint128(utxo_m.getAddress());
    }

//    public UTxO(TX tx, uint128 address) {
//        tr = new TR(address, tx.get_amount_by_address(address));
//        tx_id = tx.getTx_id();
//    }

    public UTxO_m to_utxo_m() {
        return UTxO_m.newBuilder()
                .setAddress(address.to_grpc())
                .setTxId(tx_id.to_grpc())
                .build();
    }

    public uint128 getTx_id() {
        return tx_id.clone();
    }

    public uint128 getAddress() {
        return address;
    }

    public UTxO clone() {
        return new UTxO(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UTxO)) return false;
        UTxO uTxO = (UTxO) o;
        return getTx_id().equals(uTxO.getTx_id()) && getAddress().equals(uTxO.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTx_id(), getAddress());
    }
}
