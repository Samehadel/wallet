// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/resources/proto/user_status_message.proto

package com.finance.common.event.schema.user;

/**
 * Protobuf enum {@code com.finance.common.event.schema.user.UserStatus}
 */
public enum UserStatus
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>ACTIVE = 0;</code>
   */
  ACTIVE(0),
  /**
   * <code>LOCKED = 1;</code>
   */
  LOCKED(1),
  /**
   * <code>BLOCKED = 2;</code>
   */
  BLOCKED(2),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>ACTIVE = 0;</code>
   */
  public static final int ACTIVE_VALUE = 0;
  /**
   * <code>LOCKED = 1;</code>
   */
  public static final int LOCKED_VALUE = 1;
  /**
   * <code>BLOCKED = 2;</code>
   */
  public static final int BLOCKED_VALUE = 2;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static UserStatus valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static UserStatus forNumber(int value) {
    switch (value) {
      case 0: return ACTIVE;
      case 1: return LOCKED;
      case 2: return BLOCKED;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<UserStatus>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      UserStatus> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<UserStatus>() {
          public UserStatus findValueByNumber(int number) {
            return UserStatus.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.finance.common.event.schema.user.UserStatusMessageOuterClass.getDescriptor().getEnumTypes().get(0);
  }

  private static final UserStatus[] VALUES = values();

  public static UserStatus valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private UserStatus(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:com.finance.common.event.schema.user.UserStatus)
}
