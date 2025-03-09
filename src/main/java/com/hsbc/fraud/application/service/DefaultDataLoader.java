package com.hsbc.fraud.application.service;

import com.hsbc.fraud.application.bo.AntiFraudInput;
import com.hsbc.fraud.application.bo.DeviceProfile;
import com.hsbc.fraud.application.bo.TransactionBehavior;
import com.hsbc.fraud.application.bo.UserIdentity;
import org.springframework.stereotype.Service;

@Service
public class DefaultDataLoader implements DataLoader{
    @Override
    public AntiFraudInput loadData(String customerId) {
        // For different types of customers and different transaction types,
        // the customer data to be loaded varies.

        /**
         * In a real production environment, the data sources
         * include HBase, MySQL, etc. The system should be able
         * to automatically acquire relevant data based on different
         * customer types and transaction types.
         */
        return new AntiFraudInput(new UserIdentity(),new TransactionBehavior(),new DeviceProfile(),null);
    }
}
