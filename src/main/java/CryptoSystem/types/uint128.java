package CryptoSystem.types;

import notsystemserver.grpc.uint128_m;

import java.util.Objects;

public class uint128 {
    private long low;
    private long high;

    public uint128(long high, long low){
        this.high = high;
        this.low = low;
    }

    public uint128(uint128 from) {
        this(from.high, from.low);
    }

    public uint128(uint128_m from) {
        this(from.getHigh(), from.getLow());
    }

    public uint128_m to_grpc() {
        uint128_m res = uint128_m.newBuilder().setHigh(high).setLow(low).build();
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof uint128)) return false;
        uint128 uint128 = (uint128) o;
        return low == uint128.low && high == uint128.high;
    }

    @Override
    public int hashCode() {
        return Objects.hash(low, high);
    }

    public uint128 clone() {
        return new uint128(this);
    }

    @Override
    public String toString() {
        return String.format("0x%016X",high) + String.format("%016X",low) ;
    }
}
