package org.stage.xss.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.stage.xss.core.config.XssStageConfigurer;
import org.stage.xss.core.engine.aop.XssFilterAopEngine;
import org.stage.xss.core.exception.UnknownXssFilterName;
import org.stage.xss.core.spi.TestXssFilter;

import static org.junit.jupiter.api.Assertions.*;

/*
    등록된 XssFilter가 XssFilterAop에 잘 주입되는지,
    XssFilterAop가 XssFilter에 Filtering과정을 잘 넘겨주는지 테스트함.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {XssStageConfigurer.class, TestXssFilter.class})
class XssFilterAopReigsterTest{

    @Autowired
    private XssFilterAopEngine xssFilterAop;

    @Test
    @DisplayName("Xss Filtering 파라미터 한개 성공 테스트")
    void XSS_FILTERING_ONE_PARAMETER_TEST(){
        // given
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(new XssFilterTarget());
        aspectJProxyFactory.addAspect(xssFilterAop);
        XssFilterTarget xssFilterTarget = aspectJProxyFactory.getProxy();

        String expected = "hello world";

        // when
        String result = xssFilterTarget.filteringString("bye bye");

        // then
        assertEquals(expected, result);
    }

    /*
        파라미터 두개에 @Xss 어노테이션이 마크되어있다면? -> 둘다 필터링 되어야함.
        첫번째 파라미터는 @Xss("string"), 두번째 파라미터는 @Xss로 마크되어있음.
     */
    @Test
    @DisplayName("Xss Filtering 파라미터 두개 성공 테스트")
    void XSS_FILTERING_TWO_PARAMETER_TEST(){
        // given
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(new XssFilterTarget());
        aspectJProxyFactory.addAspect(xssFilterAop);
        XssFilterTarget xssFilterTarget = aspectJProxyFactory.getProxy();

        String expected = "hello worldhello world";

        // when
        String result = xssFilterTarget.filteringTwoParameter("bye world", "bye bye");

        // then
        assertEquals(expected, result);
    }

    /*
        파라미터 둘중 한개에 @Xss 어노테이션이 마크되어 있다면? -> 마크된것만 필터링 되어야함.
     */
    @Test
    @DisplayName("Xss Filtering 파라미터 둘 중 한개 성공 테스트")
    void XSS_FILTERING_ONE_OF_TWO_PARAMETER_TEST(){
        // given
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(new XssFilterTarget());
        aspectJProxyFactory.addAspect(xssFilterAop);
        XssFilterTarget xssFilterTarget = aspectJProxyFactory.getProxy();

        String expected = "hello worldxb";

        // when
        String result = xssFilterTarget.filteringOneParameter("xb", "xb");

        // then
        assertEquals(expected, result);
    }

    /*
        @Xss 어노테이션에 잘못된 value() 가 들어있다면? -> 실패해야함.
     */
    @Test
    @DisplayName("Xss Wrong Filtering 파라미터 실패 테스트")
    void XSS_FILTERING_WRONG_XSS_VALUE_NAME_FAIL_TEST(){
        // given
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(new XssFilterTarget());
        aspectJProxyFactory.addAspect(xssFilterAop);
        XssFilterTarget xssFilterTarget = aspectJProxyFactory.getProxy();

        // when & then
        assertThrows(UnknownXssFilterName.class, () -> xssFilterTarget.filterWrongXssAnnotation(""));
    }

}
