// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: systemserver.proto

package notsystemserver.grpc;

public final class Systemserver {
  private Systemserver() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_notsystemserver_grpc_Init_Server_Args_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_notsystemserver_grpc_Init_Server_Args_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_notsystemserver_grpc_TR_m_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_notsystemserver_grpc_TR_m_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_notsystemserver_grpc_UTxO_m_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_notsystemserver_grpc_UTxO_m_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_notsystemserver_grpc_TX_m_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_notsystemserver_grpc_TX_m_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_notsystemserver_grpc_Response_status_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_notsystemserver_grpc_Response_status_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_notsystemserver_grpc_Submit_Transaction_list_Req_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_notsystemserver_grpc_Submit_Transaction_list_Req_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022systemserver.proto\022\024notsystemserver.gr" +
      "pc\"\022\n\020Init_Server_Args\"&\n\004TR_m\022\017\n\007addres" +
      "s\030\001 \001(\014\022\r\n\005coins\030\002 \001(\004\"?\n\006UTxO_m\022\r\n\005Tx_i" +
      "d\030\001 \001(\014\022&\n\002tr\030\002 \001(\0132\032.notsystemserver.gr" +
      "pc.TR_m\"\260\001\n\004TX_m\022\r\n\005Tx_id\030\001 \001(\014\022\016\n\006sende" +
      "r\030\002 \001(\014\022\020\n\010receiver\030\003 \001(\014\022\016\n\006amount\030\004 \001(" +
      "\004\022\021\n\ttimestamp\030\005 \001(\004\022+\n\005utxos\030\006 \003(\0132\034.no" +
      "tsystemserver.grpc.UTxO_m\022\'\n\003trs\030\007 \003(\0132\032" +
      ".notsystemserver.grpc.TR_m\"#\n\017Response_s" +
      "tatus\022\020\n\010response\030\001 \001(\t\"K\n\033Submit_Transa" +
      "ction_list_Req\022,\n\010requests\030\001 \003(\0132\032.notsy" +
      "stemserver.grpc.TX_m2\345\003\n\014SystemServer\022\\\n" +
      "\013init_Server\022&.notsystemserver.grpc.Init" +
      "_Server_Args\032%.notsystemserver.grpc.Resp" +
      "onse_status\022W\n\022Submit_Transaction\022\032.nots" +
      "ystemserver.grpc.TX_m\032%.notsystemserver." +
      "grpc.Response_status\022s\n\027submit_Transacti" +
      "on_list\0221.notsystemserver.grpc.Submit_Tr" +
      "ansaction_list_Req\032%.notsystemserver.grp" +
      "c.Response_status\022O\n\nSend_Coins\022\032.notsys" +
      "temserver.grpc.TX_m\032%.notsystemserver.gr" +
      "pc.Response_status\022X\n\023Publish_Transactio" +
      "n\022\032.notsystemserver.grpc.TX_m\032%.notsyste" +
      "mserver.grpc.Response_statusB\002P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_notsystemserver_grpc_Init_Server_Args_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_notsystemserver_grpc_Init_Server_Args_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_notsystemserver_grpc_Init_Server_Args_descriptor,
        new java.lang.String[] { });
    internal_static_notsystemserver_grpc_TR_m_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_notsystemserver_grpc_TR_m_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_notsystemserver_grpc_TR_m_descriptor,
        new java.lang.String[] { "Address", "Coins", });
    internal_static_notsystemserver_grpc_UTxO_m_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_notsystemserver_grpc_UTxO_m_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_notsystemserver_grpc_UTxO_m_descriptor,
        new java.lang.String[] { "TxId", "Tr", });
    internal_static_notsystemserver_grpc_TX_m_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_notsystemserver_grpc_TX_m_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_notsystemserver_grpc_TX_m_descriptor,
        new java.lang.String[] { "TxId", "Sender", "Receiver", "Amount", "Timestamp", "Utxos", "Trs", });
    internal_static_notsystemserver_grpc_Response_status_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_notsystemserver_grpc_Response_status_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_notsystemserver_grpc_Response_status_descriptor,
        new java.lang.String[] { "Response", });
    internal_static_notsystemserver_grpc_Submit_Transaction_list_Req_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_notsystemserver_grpc_Submit_Transaction_list_Req_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_notsystemserver_grpc_Submit_Transaction_list_Req_descriptor,
        new java.lang.String[] { "Requests", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
