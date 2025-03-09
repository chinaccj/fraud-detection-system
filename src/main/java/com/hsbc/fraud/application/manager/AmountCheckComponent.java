package com.hsbc.fraud.application.manager;

import com.hsbc.fraud.application.bo.AntiFraudInput;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

import java.math.BigDecimal;

@LiteflowComponent(id = "amountCheck")
public class AmountCheckComponent extends NodeComponent {
    private static final double AMOUNT_THRESHOLD = 10000;

    // 正确覆盖方法签名
//    @Override
    public void process() {
        // 实际处理逻辑（虽然这里只是条件判断，但需要保持void返回）
        AntiFraudInput context = this.getFirstContextBean();
        BigDecimal doubleAsBigDecimal = BigDecimal.valueOf(AMOUNT_THRESHOLD);

        int result = context.getTransaction().getAmount().compareTo(doubleAsBigDecimal);
        if(result >= 0){
            this.getSlot().setIfResult("amountCheck",true);
        }
        else {
            this.getSlot().setIfResult("amountCheck",false);
        }
    }



//    @Override
//    public boolean processIf() throws Exception {
//        AntiFraudInput context = this.getFirstContextBean();
//        BigDecimal doubleAsBigDecimal = BigDecimal.valueOf(AMOUNT_THRESHOLD);
//
//        int result = context.getTransaction().getAmount().compareTo(doubleAsBigDecimal);
//        if(result >= 0){
//            return true;
//        }
//        else {
//            return false;
//        }
//
//    }

}
