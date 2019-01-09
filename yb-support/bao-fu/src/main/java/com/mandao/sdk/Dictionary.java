package com.mandao.sdk;


/**
 * 字典
 */
public interface Dictionary {


    /**
     * 交易状态
     */
    enum TradingStatus implements Dictionary {
        /**
         * 已受理
         */
        ACCEPT,
        /**
         * 交易成功
         */
        SUCCESS,
        /**
         * 交易失败
         */
        FAIL,
        /**
         * 初始状态
         */
        INIT,
        /**
         * 处理中
         */
        PROCESSING
    }


    /**
     * 交易类型
     */
    enum BizType implements Dictionary {

        /**
         * 充值
         */
        RECHARGE,
        /**
         * 提现
         */
        WITHDRAW,
        /**
         * 转账
         */
        TRANSFER,
        /**
         * 消费
         */
        CONSUME
    }


    /**
     * 企业证件类型
     */
    enum EnterpriseCertType implements Dictionary {
        /**
         * 营业执照
         */
        BUSINESS_LICENCE_NO,
        /**
         * 统一社会信用代码
         */
        UNIFORM_CREDIT_CODE,
        /**
         * 三证合一代码
         */
        THREE_CERTIFICATES


    }


    /**
     * 个人证件类型
     */
    enum IndividualCertType implements Dictionary {
        /**
         * 身份证
         */
        PRC_ID
    }


    /**
     * 提现账号类型
     */
    enum WithdrawalAccountType implements Dictionary {
        TO_PRIVATE(1, "对私"),
        TO_PUBLIC(2, "对公");
        private int value;
        private String key;

        WithdrawalAccountType(int value, String key) {
            this.value = value;
            this.key = key;
        }

        public final int getAccountType() {
            return value;
        }


    }


}
