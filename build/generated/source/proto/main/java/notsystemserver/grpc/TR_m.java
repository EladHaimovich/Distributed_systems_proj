// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: systemserver.proto

package notsystemserver.grpc;

/**
 * Protobuf type {@code notsystemserver.grpc.TR_m}
 */
public final class TR_m extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:notsystemserver.grpc.TR_m)
    TR_mOrBuilder {
private static final long serialVersionUID = 0L;
  // Use TR_m.newBuilder() to construct.
  private TR_m(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private TR_m() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new TR_m();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private TR_m(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            notsystemserver.grpc.uint128_m.Builder subBuilder = null;
            if (address_ != null) {
              subBuilder = address_.toBuilder();
            }
            address_ = input.readMessage(notsystemserver.grpc.uint128_m.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(address_);
              address_ = subBuilder.buildPartial();
            }

            break;
          }
          case 16: {

            coins_ = input.readUInt64();
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return notsystemserver.grpc.Systemserver.internal_static_notsystemserver_grpc_TR_m_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return notsystemserver.grpc.Systemserver.internal_static_notsystemserver_grpc_TR_m_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            notsystemserver.grpc.TR_m.class, notsystemserver.grpc.TR_m.Builder.class);
  }

  public static final int ADDRESS_FIELD_NUMBER = 1;
  private notsystemserver.grpc.uint128_m address_;
  /**
   * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
   * @return Whether the address field is set.
   */
  @java.lang.Override
  public boolean hasAddress() {
    return address_ != null;
  }
  /**
   * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
   * @return The address.
   */
  @java.lang.Override
  public notsystemserver.grpc.uint128_m getAddress() {
    return address_ == null ? notsystemserver.grpc.uint128_m.getDefaultInstance() : address_;
  }
  /**
   * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
   */
  @java.lang.Override
  public notsystemserver.grpc.uint128_mOrBuilder getAddressOrBuilder() {
    return getAddress();
  }

  public static final int COINS_FIELD_NUMBER = 2;
  private long coins_;
  /**
   * <code>uint64 coins = 2;</code>
   * @return The coins.
   */
  @java.lang.Override
  public long getCoins() {
    return coins_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (address_ != null) {
      output.writeMessage(1, getAddress());
    }
    if (coins_ != 0L) {
      output.writeUInt64(2, coins_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (address_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getAddress());
    }
    if (coins_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(2, coins_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof notsystemserver.grpc.TR_m)) {
      return super.equals(obj);
    }
    notsystemserver.grpc.TR_m other = (notsystemserver.grpc.TR_m) obj;

    if (hasAddress() != other.hasAddress()) return false;
    if (hasAddress()) {
      if (!getAddress()
          .equals(other.getAddress())) return false;
    }
    if (getCoins()
        != other.getCoins()) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasAddress()) {
      hash = (37 * hash) + ADDRESS_FIELD_NUMBER;
      hash = (53 * hash) + getAddress().hashCode();
    }
    hash = (37 * hash) + COINS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getCoins());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static notsystemserver.grpc.TR_m parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static notsystemserver.grpc.TR_m parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static notsystemserver.grpc.TR_m parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static notsystemserver.grpc.TR_m parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static notsystemserver.grpc.TR_m parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static notsystemserver.grpc.TR_m parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static notsystemserver.grpc.TR_m parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static notsystemserver.grpc.TR_m parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static notsystemserver.grpc.TR_m parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static notsystemserver.grpc.TR_m parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static notsystemserver.grpc.TR_m parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static notsystemserver.grpc.TR_m parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(notsystemserver.grpc.TR_m prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code notsystemserver.grpc.TR_m}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:notsystemserver.grpc.TR_m)
      notsystemserver.grpc.TR_mOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return notsystemserver.grpc.Systemserver.internal_static_notsystemserver_grpc_TR_m_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return notsystemserver.grpc.Systemserver.internal_static_notsystemserver_grpc_TR_m_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              notsystemserver.grpc.TR_m.class, notsystemserver.grpc.TR_m.Builder.class);
    }

    // Construct using notsystemserver.grpc.TR_m.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (addressBuilder_ == null) {
        address_ = null;
      } else {
        address_ = null;
        addressBuilder_ = null;
      }
      coins_ = 0L;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return notsystemserver.grpc.Systemserver.internal_static_notsystemserver_grpc_TR_m_descriptor;
    }

    @java.lang.Override
    public notsystemserver.grpc.TR_m getDefaultInstanceForType() {
      return notsystemserver.grpc.TR_m.getDefaultInstance();
    }

    @java.lang.Override
    public notsystemserver.grpc.TR_m build() {
      notsystemserver.grpc.TR_m result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public notsystemserver.grpc.TR_m buildPartial() {
      notsystemserver.grpc.TR_m result = new notsystemserver.grpc.TR_m(this);
      if (addressBuilder_ == null) {
        result.address_ = address_;
      } else {
        result.address_ = addressBuilder_.build();
      }
      result.coins_ = coins_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof notsystemserver.grpc.TR_m) {
        return mergeFrom((notsystemserver.grpc.TR_m)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(notsystemserver.grpc.TR_m other) {
      if (other == notsystemserver.grpc.TR_m.getDefaultInstance()) return this;
      if (other.hasAddress()) {
        mergeAddress(other.getAddress());
      }
      if (other.getCoins() != 0L) {
        setCoins(other.getCoins());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      notsystemserver.grpc.TR_m parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (notsystemserver.grpc.TR_m) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private notsystemserver.grpc.uint128_m address_;
    private com.google.protobuf.SingleFieldBuilderV3<
        notsystemserver.grpc.uint128_m, notsystemserver.grpc.uint128_m.Builder, notsystemserver.grpc.uint128_mOrBuilder> addressBuilder_;
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     * @return Whether the address field is set.
     */
    public boolean hasAddress() {
      return addressBuilder_ != null || address_ != null;
    }
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     * @return The address.
     */
    public notsystemserver.grpc.uint128_m getAddress() {
      if (addressBuilder_ == null) {
        return address_ == null ? notsystemserver.grpc.uint128_m.getDefaultInstance() : address_;
      } else {
        return addressBuilder_.getMessage();
      }
    }
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     */
    public Builder setAddress(notsystemserver.grpc.uint128_m value) {
      if (addressBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        address_ = value;
        onChanged();
      } else {
        addressBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     */
    public Builder setAddress(
        notsystemserver.grpc.uint128_m.Builder builderForValue) {
      if (addressBuilder_ == null) {
        address_ = builderForValue.build();
        onChanged();
      } else {
        addressBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     */
    public Builder mergeAddress(notsystemserver.grpc.uint128_m value) {
      if (addressBuilder_ == null) {
        if (address_ != null) {
          address_ =
            notsystemserver.grpc.uint128_m.newBuilder(address_).mergeFrom(value).buildPartial();
        } else {
          address_ = value;
        }
        onChanged();
      } else {
        addressBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     */
    public Builder clearAddress() {
      if (addressBuilder_ == null) {
        address_ = null;
        onChanged();
      } else {
        address_ = null;
        addressBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     */
    public notsystemserver.grpc.uint128_m.Builder getAddressBuilder() {
      
      onChanged();
      return getAddressFieldBuilder().getBuilder();
    }
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     */
    public notsystemserver.grpc.uint128_mOrBuilder getAddressOrBuilder() {
      if (addressBuilder_ != null) {
        return addressBuilder_.getMessageOrBuilder();
      } else {
        return address_ == null ?
            notsystemserver.grpc.uint128_m.getDefaultInstance() : address_;
      }
    }
    /**
     * <code>.notsystemserver.grpc.uint128_m address = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        notsystemserver.grpc.uint128_m, notsystemserver.grpc.uint128_m.Builder, notsystemserver.grpc.uint128_mOrBuilder> 
        getAddressFieldBuilder() {
      if (addressBuilder_ == null) {
        addressBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            notsystemserver.grpc.uint128_m, notsystemserver.grpc.uint128_m.Builder, notsystemserver.grpc.uint128_mOrBuilder>(
                getAddress(),
                getParentForChildren(),
                isClean());
        address_ = null;
      }
      return addressBuilder_;
    }

    private long coins_ ;
    /**
     * <code>uint64 coins = 2;</code>
     * @return The coins.
     */
    @java.lang.Override
    public long getCoins() {
      return coins_;
    }
    /**
     * <code>uint64 coins = 2;</code>
     * @param value The coins to set.
     * @return This builder for chaining.
     */
    public Builder setCoins(long value) {
      
      coins_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>uint64 coins = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearCoins() {
      
      coins_ = 0L;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:notsystemserver.grpc.TR_m)
  }

  // @@protoc_insertion_point(class_scope:notsystemserver.grpc.TR_m)
  private static final notsystemserver.grpc.TR_m DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new notsystemserver.grpc.TR_m();
  }

  public static notsystemserver.grpc.TR_m getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<TR_m>
      PARSER = new com.google.protobuf.AbstractParser<TR_m>() {
    @java.lang.Override
    public TR_m parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new TR_m(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<TR_m> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<TR_m> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public notsystemserver.grpc.TR_m getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

