package com.hsbc.fraud.infrastructure.dao;

public interface AntiFraudNotify {
    public void notifyRisks(AlertEvent event);
}
