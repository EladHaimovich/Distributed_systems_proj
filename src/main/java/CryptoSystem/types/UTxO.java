package CryptoSystem.types;

import com.google.protobuf.ByteString;
import notsystemserver.grpc.*;

import java.util.Arrays;
import java.util.Objects;

public class UTxO {

    public static final int SIZE_OF_TXID = 16;
    public static final int SIZE_OF_ADDESSS = 16;
    byte []tx_id;
    TR tr;

    public UTxO(byte []tx_id, TR tr) {
        this.tx_id = tx_id.clone();
        this.tr = tr.clone();
    }

    public UTxO(UTxO_m utxo_m) {
        tx_id = utxo_m.getTxId().toByteArray().clone();
        tr = new TR(utxo_m.getTr());
    }

    public UTxO(TX tx, byte[] address) {
        tr = new TR(address, tx.get_amount_by_address(address));
        tx_id = tx.getTx_id();
    }

    public UTxO_m to_utxo_m() {
        return UTxO_m.newBuilder().setTr(tr.to_tr_m()).setTxId(ByteString.copyFrom(tx_id)).build();
    }

    public byte[] getTx_id() {
        return tx_id.clone();
    }

    public TR getTr() {
        return tr.clone();
    }

    public long getAmount() {
        return this.tr.getAmount();
    }

    public byte[] getAddress() {
        return this.tr.getAddress();
    }

    public UTxO clone() {
        return new UTxO(this.tx_id, this.tr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UTxO)) return false;
        UTxO uTxO = (UTxO) o;
        return Arrays.equals(getTx_id(), uTxO.getTx_id()) && Arrays.equals(this.getAddress(), uTxO.getAddress());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getTr());
        result = 31 * result + Arrays.hashCode(getTx_id());
        return result;
    }
}
