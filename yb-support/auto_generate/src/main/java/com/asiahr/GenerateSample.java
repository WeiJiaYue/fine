package com.asiahr;


public class GenerateSample {
    public static void main(String[] args) throws Exception {

//        ConnectionCreator connectionCreator = new DatabaseConnectionCreator
//                ("192.168.1.151",
//                        "3306",
//                        "root",
//                        "passW0rd",
//                        "asiahr_bm",
//                        "mysql");

        ConnectionCreator connectionCreator = new DatabaseConnectionCreator
                ("192.168.1.151",
                        "3306",
                        "root",
                        "passW0rd",
                        "yunbao_flexible",
                        "mysql");

        AutoGenerator codeGenerator = new GeneratorWithDBMetadata(connectionCreator);

        codeGenerator.propertiesSet(
                new AutoGenerateProperties.Builder()
                        .inProject("flexible_employment")
                        .inBasePackage("com.yunbao.flexible")
                        .neededMapper(true)
                        .neededModel(true)
                         /**
                         * 如果你的mapper.xml 是需要生成在
                         * classpath的Resources下面的话
                         * 那么你需要像下面这么做,#.generatedInResourcesPath(true)
                         * 这是正确的做法
                         */
                        .generatedInResourcesPath(true)
                        .build())
                .generateCode(
                        "salary");


    }


    /**
     *
     *

     <tx:annotation-driven transaction-manager="transactionManager"/>

     <aop:config>
     <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.*.*.service.*Service.*(..))"/>
     </aop:config>

     <!-- 实现类需要有接口，针对接口的方法,because the dynamic proxy implementation is through the jdk proxy -->
     <!--定义@Transactionl注解的实现类可以不需要接口 -->
     <tx:advice id="txAdvice" transaction-manager="transactionManager">
     <tx:attributes>
     <tx:method name="save*" propagation="REQUIRED"/>
     <tx:method name="update*" propagation="REQUIRED"/>
     <tx:method name="delete*" propagation="REQUIRED"/>
     </tx:attributes>
     </tx:advice>
     */


    /**
     * "pension_fund_info","pension_fund_order",
     "medical_fund_info","medical_fund_order","work_injury_fund_info","work_injury_fund_order","fertility_fund_info","fertility_fund_order",
     "unemployment_fund_info","unemployment_fund_order","disability_fund_order","disability_fund_info"
     ,"disease_fund_info","disease_fund_order","medical_personal_account_info","medical_personal_account_order","supplementary_medical_fund_info"
     ,"supplementary_medical_fund_order","heating_fee_info","heating_fee_order","housing_fund_info","housing_fund_order"
     */
}