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
     * 对账类型
     */
    enum TradeBillType implements Dictionary {
        /**
         * 充值
         */
        RECHARGE,
        /**
         * 提现
         */
        WITHDRAW,
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
        TO_PRIVATE("对私", 1),
        TO_PUBLIC("对公", 2);
        private int value;
        private String key;

        WithdrawalAccountType(String key, int value) {
            this.value = value;
            this.key = key;
        }

        public final int get() {
            return value;
        }


    }


    /**
     * 业务类型
     */
    enum BusinessType implements Dictionary {
        TO_PRIVATE("对私", 2),
        TO_PUBLIC("对公", 1);
        private int value;
        private String key;

        BusinessType(String key, int value) {
            this.value = value;
            this.key = key;
        }

        public final int get() {
            return value;
        }


    }

    /**
     * 账户类型
     */
    enum AccountType implements Dictionary {
        FuMin("富民银行账户", 1),
        OTHERS("他行账户", 2),
        HU_JIN("互金账户", 3);

        private String key;
        private int value;

        AccountType(String key, int value) {
            this.key = key;
            this.value = value;
        }

        public final int get() {
            return value;
        }
    }

    /**
     * 账户状态
     */
    enum AccountState implements Dictionary{
        CLOSED("关闭", 0),
        NORMAL("正常", 1),

        ;
        private String key;
        private int value;

        AccountState(String key, int value) {
            this.key = key;
            this.value = value;
        }

        public final int get() {
            return value;
        }
    }


}
