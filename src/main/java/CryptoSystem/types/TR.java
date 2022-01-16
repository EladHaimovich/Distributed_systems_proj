package CryptoSystem.types;

import com.google.protobuf.ByteString;
import notsystemserver.grpc.TR_m;

import java.util.Arrays;
import java.util.Objects;

public class TR {

    uint128 address;
    long coins;
    public TR(uint128 address, long coins) {
        this.address = address.clone();
        this.coins = coins;
    }

    public TR(TR_m tr_m) {
        address = new uint128(tr_m.getAddress());
        coins = tr_m.getCoins();
    }

    public TR_m to_grpc() {
        return TR_m.newBuilder().setAddress(address.to_grpc()).setCoins(coins).build();
    }

    public uint128 getAddress() {
        return address.clone();
    }

    public long getAmount() {
        return coins;
    }

    public TR clone() {
        return new TR(this.address, this.coins);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TR)) return false;
        TR tr = (TR) o;
        return coins == tr.coins && getAddress().equals(tr.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), coins);
    }
}
