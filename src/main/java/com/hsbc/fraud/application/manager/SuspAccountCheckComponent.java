package com.hsbc.fraud.application.manager;

import com.hsbc.fraud.application.bo.AntiFraudInput;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;


@LiteflowComponent(id = "suspAccountCheck")
public class SuspAccountCheckComponent extends NodeComponent {
    // NodeIfComponent

    @Override
    public void process() {
        AntiFraudInput context = this.getFirstContextBean();

        //Suppose the suspicious accounts are 001 and 002.
        if(context.getTransaction().getAccountId().equals("001") || context.getTransaction().getAccountId().equals("002")){
            this.getSlot().setIfResult("suspAccountCheck",true);
        }
        else {
            this.getSlot().setIfResult("suspAccountCheck",false);
        }
    }


//    @Override
//    public boolean processIf() throws Exception {
//        AntiFraudInput context = this.getFirstContextBean();
//
//        //Suppose the suspicious accounts are 001 and 002.
//        if (context.getTransaction().getAccountId().equals("001") || context.getTransaction().getAccountId().equals("002")) {
////            this.getSlot().setIfResult("isSuspAccount", true);
//            return true;
//        } else {
////            this.getSlot().setIfResult("isSuspAccount", false);
//            return false;
//        }
//    }

}