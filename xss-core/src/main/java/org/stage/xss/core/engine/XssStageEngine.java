package org.stage.xss.core.engine;

/**
 * {@link org.stage.xss.core.spi.XssFilter} 구현체들을 파라미터에 적용하는 역할을 한다.
 *
 * @param <T> XssStageEngine 구현체의 메소드가 반환하는 타입이다.
 * @param <P> XssStageEngine 구헨체가 받는 파라미터 값이다.
 *
 * @since 2.0
 * @author devxb
 */
public interface XssStageEngine<T, P>{

    /**
     * 어떠한, Xss filtering이 필요한 파라미터를 받아 Xss safe한 상태를 반환한다.
     *
     * @param parameter 이 파라미터는 Xss filtering 대상인 어떤것이다.
     * @return Xss safe한 상태를 반환한다.
     *
     * @since 2.0
     */
    T doFiltering(P parameter) throws Throwable;

}
