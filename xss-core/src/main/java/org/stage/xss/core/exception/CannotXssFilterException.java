package org.stage.xss.core.exception;

/**
 * XssFiltering 중 필터링 할 수 없을때 이 예외가 던져짐.
 *
 * @since 0.1
 * @author devxb
 */
public final class CannotXssFilterException extends RuntimeException{

    /**
     * @param target Xss filtering 중 예외가 발생한 대상의 패키지명.클래스이름
     */
    public CannotXssFilterException(String target){
        super("Non-filterable target \"" + target + "\"");
    }

}
