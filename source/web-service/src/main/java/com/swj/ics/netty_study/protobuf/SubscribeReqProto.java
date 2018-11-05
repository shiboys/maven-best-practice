// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: github/maven-best-practice/source/web-service/src/main/java/com/swj/ics/netty_study/protobuf/SubscribeReq.proto

package com.swj.ics.netty_study.protobuf;

public final class SubscribeReqProto {
  private SubscribeReqProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface SubscribeReqOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // required int32 subReqId = 1;
    /**
     * <code>required int32 subReqId = 1;</code>
     */
    boolean hasSubReqId();
    /**
     * <code>required int32 subReqId = 1;</code>
     */
    int getSubReqId();

    // required string username = 2;
    /**
     * <code>required string username = 2;</code>
     */
    boolean hasUsername();
    /**
     * <code>required string username = 2;</code>
     */
    String getUsername();
    /**
     * <code>required string username = 2;</code>
     */
    com.google.protobuf.ByteString
        getUsernameBytes();

    // required string productName = 3;
    /**
     * <code>required string productName = 3;</code>
     */
    boolean hasProductName();
    /**
     * <code>required string productName = 3;</code>
     */
    String getProductName();
    /**
     * <code>required string productName = 3;</code>
     */
    com.google.protobuf.ByteString
        getProductNameBytes();

    // repeated string addressList = 4;
    /**
     * <code>repeated string addressList = 4;</code>
     */
    java.util.List<String>
    getAddressListList();
    /**
     * <code>repeated string addressList = 4;</code>
     */
    int getAddressListCount();
    /**
     * <code>repeated string addressList = 4;</code>
     */
    String getAddressList(int index);
    /**
     * <code>repeated string addressList = 4;</code>
     */
    com.google.protobuf.ByteString
        getAddressListBytes(int index);
  }
  /**
   * Protobuf type {@code netty_study.protobuf.SubscribeReq}
   */
  public static final class SubscribeReq extends
      com.google.protobuf.GeneratedMessage
      implements SubscribeReqOrBuilder {
    // Use SubscribeReq.newBuilder() to construct.
    private SubscribeReq(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private SubscribeReq(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final SubscribeReq defaultInstance;
    public static SubscribeReq getDefaultInstance() {
      return defaultInstance;
    }

    public SubscribeReq getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private SubscribeReq(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
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
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              subReqId_ = input.readInt32();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              username_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              productName_ = input.readBytes();
              break;
            }
            case 34: {
              if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
                addressList_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000008;
              }
              addressList_.add(input.readBytes());
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
          addressList_ = new com.google.protobuf.UnmodifiableLazyStringList(addressList_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return SubscribeReqProto.internal_static_netty_study_protobuf_SubscribeReq_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return SubscribeReqProto.internal_static_netty_study_protobuf_SubscribeReq_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              SubscribeReq.class, Builder.class);
    }

    public static com.google.protobuf.Parser<SubscribeReq> PARSER =
        new com.google.protobuf.AbstractParser<SubscribeReq>() {
      public SubscribeReq parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new SubscribeReq(input, extensionRegistry);
      }
    };

    @Override
    public com.google.protobuf.Parser<SubscribeReq> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // required int32 subReqId = 1;
    public static final int SUBREQID_FIELD_NUMBER = 1;
    private int subReqId_;
    /**
     * <code>required int32 subReqId = 1;</code>
     */
    public boolean hasSubReqId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required int32 subReqId = 1;</code>
     */
    public int getSubReqId() {
      return subReqId_;
    }

    // required string username = 2;
    public static final int USERNAME_FIELD_NUMBER = 2;
    private Object username_;
    /**
     * <code>required string username = 2;</code>
     */
    public boolean hasUsername() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required string username = 2;</code>
     */
    public String getUsername() {
      Object ref = username_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          username_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string username = 2;</code>
     */
    public com.google.protobuf.ByteString
        getUsernameBytes() {
      Object ref = username_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        username_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    // required string productName = 3;
    public static final int PRODUCTNAME_FIELD_NUMBER = 3;
    private Object productName_;
    /**
     * <code>required string productName = 3;</code>
     */
    public boolean hasProductName() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>required string productName = 3;</code>
     */
    public String getProductName() {
      Object ref = productName_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          productName_ = s;
        }
        return s;
      }
    }
    /**
     * <code>required string productName = 3;</code>
     */
    public com.google.protobuf.ByteString
        getProductNameBytes() {
      Object ref = productName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        productName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    // repeated string addressList = 4;
    public static final int ADDRESSLIST_FIELD_NUMBER = 4;
    private com.google.protobuf.LazyStringList addressList_;
    /**
     * <code>repeated string addressList = 4;</code>
     */
    public java.util.List<String>
        getAddressListList() {
      return addressList_;
    }
    /**
     * <code>repeated string addressList = 4;</code>
     */
    public int getAddressListCount() {
      return addressList_.size();
    }
    /**
     * <code>repeated string addressList = 4;</code>
     */
    public String getAddressList(int index) {
      return addressList_.get(index);
    }
    /**
     * <code>repeated string addressList = 4;</code>
     */
    public com.google.protobuf.ByteString
        getAddressListBytes(int index) {
      return addressList_.getByteString(index);
    }

    private void initFields() {
      subReqId_ = 0;
      username_ = "";
      productName_ = "";
      addressList_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (!hasSubReqId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasUsername()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasProductName()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, subReqId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getUsernameBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getProductNameBytes());
      }
      for (int i = 0; i < addressList_.size(); i++) {
        output.writeBytes(4, addressList_.getByteString(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, subReqId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getUsernameBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getProductNameBytes());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < addressList_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(addressList_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getAddressListList().size();
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    protected Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static SubscribeReq parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static SubscribeReq parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static SubscribeReq parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static SubscribeReq parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static SubscribeReq parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static SubscribeReq parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static SubscribeReq parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static SubscribeReq parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static SubscribeReq parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static SubscribeReq parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(SubscribeReq prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code netty_study.protobuf.SubscribeReq}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements SubscribeReqOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return SubscribeReqProto.internal_static_netty_study_protobuf_SubscribeReq_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return SubscribeReqProto.internal_static_netty_study_protobuf_SubscribeReq_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                SubscribeReq.class, Builder.class);
      }

      // Construct using com.swj.ics.netty_study.protobuf.SubscribeReqProto.SubscribeReq.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        subReqId_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        username_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        productName_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        addressList_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return SubscribeReqProto.internal_static_netty_study_protobuf_SubscribeReq_descriptor;
      }

      public SubscribeReq getDefaultInstanceForType() {
        return SubscribeReq.getDefaultInstance();
      }

      public SubscribeReq build() {
        SubscribeReq result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public SubscribeReq buildPartial() {
        SubscribeReq result = new SubscribeReq(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.subReqId_ = subReqId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.username_ = username_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.productName_ = productName_;
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          addressList_ = new com.google.protobuf.UnmodifiableLazyStringList(
              addressList_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.addressList_ = addressList_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof SubscribeReq) {
          return mergeFrom((SubscribeReq)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(SubscribeReq other) {
        if (other == SubscribeReq.getDefaultInstance()) return this;
        if (other.hasSubReqId()) {
          setSubReqId(other.getSubReqId());
        }
        if (other.hasUsername()) {
          bitField0_ |= 0x00000002;
          username_ = other.username_;
          onChanged();
        }
        if (other.hasProductName()) {
          bitField0_ |= 0x00000004;
          productName_ = other.productName_;
          onChanged();
        }
        if (!other.addressList_.isEmpty()) {
          if (addressList_.isEmpty()) {
            addressList_ = other.addressList_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensureAddressListIsMutable();
            addressList_.addAll(other.addressList_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasSubReqId()) {
          
          return false;
        }
        if (!hasUsername()) {
          
          return false;
        }
        if (!hasProductName()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        SubscribeReq parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (SubscribeReq) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // required int32 subReqId = 1;
      private int subReqId_ ;
      /**
       * <code>required int32 subReqId = 1;</code>
       */
      public boolean hasSubReqId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required int32 subReqId = 1;</code>
       */
      public int getSubReqId() {
        return subReqId_;
      }
      /**
       * <code>required int32 subReqId = 1;</code>
       */
      public Builder setSubReqId(int value) {
        bitField0_ |= 0x00000001;
        subReqId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int32 subReqId = 1;</code>
       */
      public Builder clearSubReqId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        subReqId_ = 0;
        onChanged();
        return this;
      }

      // required string username = 2;
      private Object username_ = "";
      /**
       * <code>required string username = 2;</code>
       */
      public boolean hasUsername() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required string username = 2;</code>
       */
      public String getUsername() {
        Object ref = username_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          username_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>required string username = 2;</code>
       */
      public com.google.protobuf.ByteString
          getUsernameBytes() {
        Object ref = username_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          username_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string username = 2;</code>
       */
      public Builder setUsername(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        username_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string username = 2;</code>
       */
      public Builder clearUsername() {
        bitField0_ = (bitField0_ & ~0x00000002);
        username_ = getDefaultInstance().getUsername();
        onChanged();
        return this;
      }
      /**
       * <code>required string username = 2;</code>
       */
      public Builder setUsernameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        username_ = value;
        onChanged();
        return this;
      }

      // required string productName = 3;
      private Object productName_ = "";
      /**
       * <code>required string productName = 3;</code>
       */
      public boolean hasProductName() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>required string productName = 3;</code>
       */
      public String getProductName() {
        Object ref = productName_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          productName_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>required string productName = 3;</code>
       */
      public com.google.protobuf.ByteString
          getProductNameBytes() {
        Object ref = productName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          productName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>required string productName = 3;</code>
       */
      public Builder setProductName(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        productName_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required string productName = 3;</code>
       */
      public Builder clearProductName() {
        bitField0_ = (bitField0_ & ~0x00000004);
        productName_ = getDefaultInstance().getProductName();
        onChanged();
        return this;
      }
      /**
       * <code>required string productName = 3;</code>
       */
      public Builder setProductNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        productName_ = value;
        onChanged();
        return this;
      }

      // repeated string addressList = 4;
      private com.google.protobuf.LazyStringList addressList_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureAddressListIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          addressList_ = new com.google.protobuf.LazyStringArrayList(addressList_);
          bitField0_ |= 0x00000008;
         }
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public java.util.List<String>
          getAddressListList() {
        return java.util.Collections.unmodifiableList(addressList_);
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public int getAddressListCount() {
        return addressList_.size();
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public String getAddressList(int index) {
        return addressList_.get(index);
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public com.google.protobuf.ByteString
          getAddressListBytes(int index) {
        return addressList_.getByteString(index);
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public Builder setAddressList(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureAddressListIsMutable();
        addressList_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public Builder addAddressList(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureAddressListIsMutable();
        addressList_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public Builder addAllAddressList(
          Iterable<String> values) {
        ensureAddressListIsMutable();
        super.addAll(values, addressList_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public Builder clearAddressList() {
        addressList_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
        return this;
      }
      /**
       * <code>repeated string addressList = 4;</code>
       */
      public Builder addAddressListBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureAddressListIsMutable();
        addressList_.add(value);
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:netty_study.protobuf.SubscribeReq)
    }

    static {
      defaultInstance = new SubscribeReq(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:netty_study.protobuf.SubscribeReq)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_netty_study_protobuf_SubscribeReq_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_netty_study_protobuf_SubscribeReq_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\nogithub/maven-best-practice/source/web-" +
      "service/src/main/java/com/swj/ics/netty_" +
      "study/protobuf/SubscribeReq.proto\022\024netty" +
      "_study.protobuf\"\\\n\014SubscribeReq\022\020\n\010subRe" +
      "qId\030\001 \002(\005\022\020\n\010username\030\002 \002(\t\022\023\n\013productNa" +
      "me\030\003 \002(\t\022\023\n\013addressList\030\004 \003(\tB5\n com.swj" +
      ".ics.netty_study.protobufB\021SubscribeReqP" +
      "roto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_netty_study_protobuf_SubscribeReq_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_netty_study_protobuf_SubscribeReq_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_netty_study_protobuf_SubscribeReq_descriptor,
              new String[] { "SubReqId", "Username", "ProductName", "AddressList", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}