// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: systemserver.proto

package notsystemserver.grpc;

public interface Send_Coins_reqOrBuilder extends
    // @@protoc_insertion_point(interface_extends:notsystemserver.grpc.Send_Coins_req)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.notsystemserver.grpc.uint128_m sender = 1;</code>
   * @return Whether the sender field is set.
   */
  boolean hasSender();
  /**
   * <code>.notsystemserver.grpc.uint128_m sender = 1;</code>
   * @return The sender.
   */
  notsystemserver.grpc.uint128_m getSender();
  /**
   * <code>.notsystemserver.grpc.uint128_m sender = 1;</code>
   */
  notsystemserver.grpc.uint128_mOrBuilder getSenderOrBuilder();

  /**
   * <code>.notsystemserver.grpc.uint128_m receiver = 2;</code>
   * @return Whether the receiver field is set.
   */
  boolean hasReceiver();
  /**
   * <code>.notsystemserver.grpc.uint128_m receiver = 2;</code>
   * @return The receiver.
   */
  notsystemserver.grpc.uint128_m getReceiver();
  /**
   * <code>.notsystemserver.grpc.uint128_m receiver = 2;</code>
   */
  notsystemserver.grpc.uint128_mOrBuilder getReceiverOrBuilder();

  /**
   * <code>uint64 coins = 3;</code>
   * @return The coins.
   */
  long getCoins();
}
