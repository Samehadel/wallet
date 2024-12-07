// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/resources/proto/user_status_message.proto

package com.finance.common.event.schema.user;

public interface UserStatusMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.finance.common.event.schema.user.UserStatusMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 user_id = 1;</code>
   * @return The userId.
   */
  long getUserId();

  /**
   * <code>.com.finance.common.event.schema.user.UserStatus new_status = 2;</code>
   * @return The enum numeric value on the wire for newStatus.
   */
  int getNewStatusValue();
  /**
   * <code>.com.finance.common.event.schema.user.UserStatus new_status = 2;</code>
   * @return The newStatus.
   */
  com.finance.common.event.schema.user.UserStatus getNewStatus();

  /**
   * <code>string reason = 3;</code>
   * @return The reason.
   */
  java.lang.String getReason();
  /**
   * <code>string reason = 3;</code>
   * @return The bytes for reason.
   */
  com.google.protobuf.ByteString
      getReasonBytes();

  /**
   * <code>int64 new_status_time = 4;</code>
   * @return The newStatusTime.
   */
  long getNewStatusTime();

  /**
   * <code>.com.finance.common.event.schema.user.EventTypes event_type = 5;</code>
   * @return The enum numeric value on the wire for eventType.
   */
  int getEventTypeValue();
  /**
   * <code>.com.finance.common.event.schema.user.EventTypes event_type = 5;</code>
   * @return The eventType.
   */
  com.finance.common.event.schema.user.EventTypes getEventType();
}
