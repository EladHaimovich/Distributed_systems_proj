package CryptoSystem.types;

import com.google.protobuf.ByteString;
import notsystemserver.grpc.TR_m;

import java.util.Arrays;
import java.util.Objects;

public class TR {

    byte[] address;
    long amount;
    public TR(byte[] address, long coins) {
        this.address = address.clone();
        this.amount = coins;
    }

    public TR(TR_m tr_m) {
        address = tr_m.getAddress().toByteArray().clone();
        amount = tr_m.getCoins();
    }

    public TR_m to_tr_m() {
        return TR_m.newBuilder().setAddress(ByteString.copyFrom(address)).setCoins(amount).build();
    }

    public byte[] getAddress() {
        return address.clone();
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TR)) return false;
        TR tr = (TR) o;
        return getAmount() == tr.getAmount() && Arrays.equals(getAddress(), tr.getAddress());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getAmount());
        result = 31 * result + Arrays.hashCode(getAddress());
        return result;
    }

    public TR clone() {
        return new TR(this.address, this.amount);
    }
}
