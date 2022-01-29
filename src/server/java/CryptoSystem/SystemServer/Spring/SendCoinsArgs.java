package CryptoSystem.SystemServer.Spring;

import CryptoSystem.types.uint128;

public class SendCoinsArgs {
    uint128 sender;
    uint128 receiver;
    long amount;

    SendCoinsArgs(uint128 sender, uint128 receiver, long amount) {
        this.sender = new uint128(sender);
        this.receiver = new uint128(receiver);
        this.amount = amount;
    }

    SendCoinsArgs() {
        this.sender = new uint128(0,0);
        this.receiver = new uint128(0,0);
        this.amount = 0;
    }

    public long getAmount() {
        return amount;
    }

    public uint128 getReceiver() {
        return receiver;
    }

    public uint128 getSender() {
        return sender;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setReceiver(uint128 receiver) {
        this.receiver = new uint128(receiver);
    }

    public void setSender(uint128 sender) {
        this.sender = new uint128(sender);
    }
}
