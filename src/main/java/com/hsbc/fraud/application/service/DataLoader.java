package com.hsbc.fraud.application.service;

import com.hsbc.fraud.application.bo.AntiFraudInput;

public interface DataLoader {
    AntiFraudInput loadData(String customerId);
}
