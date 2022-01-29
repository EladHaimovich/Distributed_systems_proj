package CryptoSystem.SystemServer.Spring;

import CryptoSystem.types.uint128;

public class GetArgs {
    uint128 address;
    int amount;

    GetArgs(uint128 address, int amount) {
        if (address != null)
            this.address = new uint128(address);
        else
            this.address = null;
        this.amount = amount;
    }

    public uint128 getAddress() {
        return this.address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setAddress(uint128 address) {
        if (address != null)
            this.address = new uint128(address);
        else
            this.address = null;
    }



}
