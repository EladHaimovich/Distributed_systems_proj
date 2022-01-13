package notsystemserver.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.43.1)",
    comments = "Source: systemserver.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SystemServerGrpc {

  private SystemServerGrpc() {}

  public static final String SERVICE_NAME = "notsystemserver.grpc.SystemServer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<notsystemserver.grpc.Init_Server_Args,
      notsystemserver.grpc.Response_status> getInitServerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "init_Server",
      requestType = notsystemserver.grpc.Init_Server_Args.class,
      responseType = notsystemserver.grpc.Response_status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<notsystemserver.grpc.Init_Server_Args,
      notsystemserver.grpc.Response_status> getInitServerMethod() {
    io.grpc.MethodDescriptor<notsystemserver.grpc.Init_Server_Args, notsystemserver.grpc.Response_status> getInitServerMethod;
    if ((getInitServerMethod = SystemServerGrpc.getInitServerMethod) == null) {
      synchronized (SystemServerGrpc.class) {
        if ((getInitServerMethod = SystemServerGrpc.getInitServerMethod) == null) {
          SystemServerGrpc.getInitServerMethod = getInitServerMethod =
              io.grpc.MethodDescriptor.<notsystemserver.grpc.Init_Server_Args, notsystemserver.grpc.Response_status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "init_Server"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.Init_Server_Args.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.Response_status.getDefaultInstance()))
              .setSchemaDescriptor(new SystemServerMethodDescriptorSupplier("init_Server"))
              .build();
        }
      }
    }
    return getInitServerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<notsystemserver.grpc.TX_m,
      notsystemserver.grpc.Response_status> getSubmitTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Submit_Transaction",
      requestType = notsystemserver.grpc.TX_m.class,
      responseType = notsystemserver.grpc.Response_status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<notsystemserver.grpc.TX_m,
      notsystemserver.grpc.Response_status> getSubmitTransactionMethod() {
    io.grpc.MethodDescriptor<notsystemserver.grpc.TX_m, notsystemserver.grpc.Response_status> getSubmitTransactionMethod;
    if ((getSubmitTransactionMethod = SystemServerGrpc.getSubmitTransactionMethod) == null) {
      synchronized (SystemServerGrpc.class) {
        if ((getSubmitTransactionMethod = SystemServerGrpc.getSubmitTransactionMethod) == null) {
          SystemServerGrpc.getSubmitTransactionMethod = getSubmitTransactionMethod =
              io.grpc.MethodDescriptor.<notsystemserver.grpc.TX_m, notsystemserver.grpc.Response_status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Submit_Transaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.TX_m.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.Response_status.getDefaultInstance()))
              .setSchemaDescriptor(new SystemServerMethodDescriptorSupplier("Submit_Transaction"))
              .build();
        }
      }
    }
    return getSubmitTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<notsystemserver.grpc.Submit_Transaction_list_Req,
      notsystemserver.grpc.Response_status> getSubmitTransactionListMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "submit_Transaction_list",
      requestType = notsystemserver.grpc.Submit_Transaction_list_Req.class,
      responseType = notsystemserver.grpc.Response_status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<notsystemserver.grpc.Submit_Transaction_list_Req,
      notsystemserver.grpc.Response_status> getSubmitTransactionListMethod() {
    io.grpc.MethodDescriptor<notsystemserver.grpc.Submit_Transaction_list_Req, notsystemserver.grpc.Response_status> getSubmitTransactionListMethod;
    if ((getSubmitTransactionListMethod = SystemServerGrpc.getSubmitTransactionListMethod) == null) {
      synchronized (SystemServerGrpc.class) {
        if ((getSubmitTransactionListMethod = SystemServerGrpc.getSubmitTransactionListMethod) == null) {
          SystemServerGrpc.getSubmitTransactionListMethod = getSubmitTransactionListMethod =
              io.grpc.MethodDescriptor.<notsystemserver.grpc.Submit_Transaction_list_Req, notsystemserver.grpc.Response_status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "submit_Transaction_list"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.Submit_Transaction_list_Req.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.Response_status.getDefaultInstance()))
              .setSchemaDescriptor(new SystemServerMethodDescriptorSupplier("submit_Transaction_list"))
              .build();
        }
      }
    }
    return getSubmitTransactionListMethod;
  }

  private static volatile io.grpc.MethodDescriptor<notsystemserver.grpc.TX_m,
      notsystemserver.grpc.Response_status> getSendCoinsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Send_Coins",
      requestType = notsystemserver.grpc.TX_m.class,
      responseType = notsystemserver.grpc.Response_status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<notsystemserver.grpc.TX_m,
      notsystemserver.grpc.Response_status> getSendCoinsMethod() {
    io.grpc.MethodDescriptor<notsystemserver.grpc.TX_m, notsystemserver.grpc.Response_status> getSendCoinsMethod;
    if ((getSendCoinsMethod = SystemServerGrpc.getSendCoinsMethod) == null) {
      synchronized (SystemServerGrpc.class) {
        if ((getSendCoinsMethod = SystemServerGrpc.getSendCoinsMethod) == null) {
          SystemServerGrpc.getSendCoinsMethod = getSendCoinsMethod =
              io.grpc.MethodDescriptor.<notsystemserver.grpc.TX_m, notsystemserver.grpc.Response_status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Send_Coins"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.TX_m.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.Response_status.getDefaultInstance()))
              .setSchemaDescriptor(new SystemServerMethodDescriptorSupplier("Send_Coins"))
              .build();
        }
      }
    }
    return getSendCoinsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<notsystemserver.grpc.Submit_Transaction_list_Req,
      notsystemserver.grpc.Response_status> getPublishTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Publish_Transaction",
      requestType = notsystemserver.grpc.Submit_Transaction_list_Req.class,
      responseType = notsystemserver.grpc.Response_status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<notsystemserver.grpc.Submit_Transaction_list_Req,
      notsystemserver.grpc.Response_status> getPublishTransactionMethod() {
    io.grpc.MethodDescriptor<notsystemserver.grpc.Submit_Transaction_list_Req, notsystemserver.grpc.Response_status> getPublishTransactionMethod;
    if ((getPublishTransactionMethod = SystemServerGrpc.getPublishTransactionMethod) == null) {
      synchronized (SystemServerGrpc.class) {
        if ((getPublishTransactionMethod = SystemServerGrpc.getPublishTransactionMethod) == null) {
          SystemServerGrpc.getPublishTransactionMethod = getPublishTransactionMethod =
              io.grpc.MethodDescriptor.<notsystemserver.grpc.Submit_Transaction_list_Req, notsystemserver.grpc.Response_status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Publish_Transaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.Submit_Transaction_list_Req.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  notsystemserver.grpc.Response_status.getDefaultInstance()))
              .setSchemaDescriptor(new SystemServerMethodDescriptorSupplier("Publish_Transaction"))
              .build();
        }
      }
    }
    return getPublishTransactionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SystemServerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SystemServerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SystemServerStub>() {
        @java.lang.Override
        public SystemServerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SystemServerStub(channel, callOptions);
        }
      };
    return SystemServerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SystemServerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SystemServerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SystemServerBlockingStub>() {
        @java.lang.Override
        public SystemServerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SystemServerBlockingStub(channel, callOptions);
        }
      };
    return SystemServerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SystemServerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SystemServerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SystemServerFutureStub>() {
        @java.lang.Override
        public SystemServerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SystemServerFutureStub(channel, callOptions);
        }
      };
    return SystemServerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class SystemServerImplBase implements io.grpc.BindableService {

    /**
     */
    public void initServer(notsystemserver.grpc.Init_Server_Args request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInitServerMethod(), responseObserver);
    }

    /**
     */
    public void submitTransaction(notsystemserver.grpc.TX_m request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubmitTransactionMethod(), responseObserver);
    }

    /**
     */
    public void submitTransactionList(notsystemserver.grpc.Submit_Transaction_list_Req request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubmitTransactionListMethod(), responseObserver);
    }

    /**
     */
    public void sendCoins(notsystemserver.grpc.TX_m request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendCoinsMethod(), responseObserver);
    }

    /**
     */
    public void publishTransaction(notsystemserver.grpc.Submit_Transaction_list_Req request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPublishTransactionMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInitServerMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                notsystemserver.grpc.Init_Server_Args,
                notsystemserver.grpc.Response_status>(
                  this, METHODID_INIT_SERVER)))
          .addMethod(
            getSubmitTransactionMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                notsystemserver.grpc.TX_m,
                notsystemserver.grpc.Response_status>(
                  this, METHODID_SUBMIT_TRANSACTION)))
          .addMethod(
            getSubmitTransactionListMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                notsystemserver.grpc.Submit_Transaction_list_Req,
                notsystemserver.grpc.Response_status>(
                  this, METHODID_SUBMIT_TRANSACTION_LIST)))
          .addMethod(
            getSendCoinsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                notsystemserver.grpc.TX_m,
                notsystemserver.grpc.Response_status>(
                  this, METHODID_SEND_COINS)))
          .addMethod(
            getPublishTransactionMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                notsystemserver.grpc.Submit_Transaction_list_Req,
                notsystemserver.grpc.Response_status>(
                  this, METHODID_PUBLISH_TRANSACTION)))
          .build();
    }
  }

  /**
   */
  public static final class SystemServerStub extends io.grpc.stub.AbstractAsyncStub<SystemServerStub> {
    private SystemServerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SystemServerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SystemServerStub(channel, callOptions);
    }

    /**
     */
    public void initServer(notsystemserver.grpc.Init_Server_Args request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInitServerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void submitTransaction(notsystemserver.grpc.TX_m request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubmitTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void submitTransactionList(notsystemserver.grpc.Submit_Transaction_list_Req request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubmitTransactionListMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendCoins(notsystemserver.grpc.TX_m request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendCoinsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void publishTransaction(notsystemserver.grpc.Submit_Transaction_list_Req request,
        io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPublishTransactionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SystemServerBlockingStub extends io.grpc.stub.AbstractBlockingStub<SystemServerBlockingStub> {
    private SystemServerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SystemServerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SystemServerBlockingStub(channel, callOptions);
    }

    /**
     */
    public notsystemserver.grpc.Response_status initServer(notsystemserver.grpc.Init_Server_Args request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInitServerMethod(), getCallOptions(), request);
    }

    /**
     */
    public notsystemserver.grpc.Response_status submitTransaction(notsystemserver.grpc.TX_m request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubmitTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public notsystemserver.grpc.Response_status submitTransactionList(notsystemserver.grpc.Submit_Transaction_list_Req request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubmitTransactionListMethod(), getCallOptions(), request);
    }

    /**
     */
    public notsystemserver.grpc.Response_status sendCoins(notsystemserver.grpc.TX_m request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendCoinsMethod(), getCallOptions(), request);
    }

    /**
     */
    public notsystemserver.grpc.Response_status publishTransaction(notsystemserver.grpc.Submit_Transaction_list_Req request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPublishTransactionMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SystemServerFutureStub extends io.grpc.stub.AbstractFutureStub<SystemServerFutureStub> {
    private SystemServerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SystemServerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SystemServerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<notsystemserver.grpc.Response_status> initServer(
        notsystemserver.grpc.Init_Server_Args request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInitServerMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<notsystemserver.grpc.Response_status> submitTransaction(
        notsystemserver.grpc.TX_m request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubmitTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<notsystemserver.grpc.Response_status> submitTransactionList(
        notsystemserver.grpc.Submit_Transaction_list_Req request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubmitTransactionListMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<notsystemserver.grpc.Response_status> sendCoins(
        notsystemserver.grpc.TX_m request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendCoinsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<notsystemserver.grpc.Response_status> publishTransaction(
        notsystemserver.grpc.Submit_Transaction_list_Req request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPublishTransactionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INIT_SERVER = 0;
  private static final int METHODID_SUBMIT_TRANSACTION = 1;
  private static final int METHODID_SUBMIT_TRANSACTION_LIST = 2;
  private static final int METHODID_SEND_COINS = 3;
  private static final int METHODID_PUBLISH_TRANSACTION = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SystemServerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SystemServerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INIT_SERVER:
          serviceImpl.initServer((notsystemserver.grpc.Init_Server_Args) request,
              (io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status>) responseObserver);
          break;
        case METHODID_SUBMIT_TRANSACTION:
          serviceImpl.submitTransaction((notsystemserver.grpc.TX_m) request,
              (io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status>) responseObserver);
          break;
        case METHODID_SUBMIT_TRANSACTION_LIST:
          serviceImpl.submitTransactionList((notsystemserver.grpc.Submit_Transaction_list_Req) request,
              (io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status>) responseObserver);
          break;
        case METHODID_SEND_COINS:
          serviceImpl.sendCoins((notsystemserver.grpc.TX_m) request,
              (io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status>) responseObserver);
          break;
        case METHODID_PUBLISH_TRANSACTION:
          serviceImpl.publishTransaction((notsystemserver.grpc.Submit_Transaction_list_Req) request,
              (io.grpc.stub.StreamObserver<notsystemserver.grpc.Response_status>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class SystemServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SystemServerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return notsystemserver.grpc.Systemserver.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SystemServer");
    }
  }

  private static final class SystemServerFileDescriptorSupplier
      extends SystemServerBaseDescriptorSupplier {
    SystemServerFileDescriptorSupplier() {}
  }

  private static final class SystemServerMethodDescriptorSupplier
      extends SystemServerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SystemServerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SystemServerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SystemServerFileDescriptorSupplier())
              .addMethod(getInitServerMethod())
              .addMethod(getSubmitTransactionMethod())
              .addMethod(getSubmitTransactionListMethod())
              .addMethod(getSendCoinsMethod())
              .addMethod(getPublishTransactionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
