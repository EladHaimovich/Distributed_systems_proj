// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: systemserver.proto

package notsystemserver.grpc;

public interface get_transactions_mOrBuilder extends
    // @@protoc_insertion_point(interface_extends:notsystemserver.grpc.get_transactions_m)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bool specific_address = 1;</code>
   * @return The specificAddress.
   */
  boolean getSpecificAddress();

  /**
   * <code>.notsystemserver.grpc.uint128_m address = 2;</code>
   * @return Whether the address field is set.
   */
  boolean hasAddress();
  /**
   * <code>.notsystemserver.grpc.uint128_m address = 2;</code>
   * @return The address.
   */
  notsystemserver.grpc.uint128_m getAddress();
  /**
   * <code>.notsystemserver.grpc.uint128_m address = 2;</code>
   */
  notsystemserver.grpc.uint128_mOrBuilder getAddressOrBuilder();

  /**
   * <code>string lock_path = 3;</code>
   * @return The lockPath.
   */
  java.lang.String getLockPath();
  /**
   * <code>string lock_path = 3;</code>
   * @return The bytes for lockPath.
   */
  com.google.protobuf.ByteString
      getLockPathBytes();
}
