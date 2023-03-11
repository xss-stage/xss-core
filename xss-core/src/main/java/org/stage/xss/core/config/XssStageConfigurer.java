package org.stage.xss.core.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.stage.xss.core.engine.aop.XssFilterAopEngine;
import org.stage.xss.core.spi.XssFilter;

@Configuration
public class XssStageConfigurer{

    @Autowired
    List<XssFilter> xssFilterList;

    @Bean
    public XssFilterAopEngine xssFilterAop(){
        return new XssFilterAopEngine(xssFilterList);
    }

}
