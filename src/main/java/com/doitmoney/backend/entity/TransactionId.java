package com.doitmoney.backend.entity;


import java.io.Serializable;
import java.util.Objects;

public class TransactionId implements Serializable {
    private Long userId;
    private Integer id;

    public TransactionId() {}

    public TransactionId(Long userId, Integer id) {
        this.userId = userId;
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        TransactionId that = (TransactionId) obj;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(id, that.id);
    }
}