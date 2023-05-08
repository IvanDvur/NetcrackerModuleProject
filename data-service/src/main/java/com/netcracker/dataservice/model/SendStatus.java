package com.netcracker.dataservice.model;

/**
 * Статус отправки того или иного сообщения в SendManager
 */
public enum SendStatus {
    WAITING,PROCESSING,PROCESSED,SENT,FAILED,NOT_SENT
}
