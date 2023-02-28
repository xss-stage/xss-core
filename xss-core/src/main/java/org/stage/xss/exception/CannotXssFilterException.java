package org.stage.xss.exception;

public final class CannotXssFilterException extends RuntimeException{

    public CannotXssFilterException(String target){
        super("Non-filterable target \"" + target + "\"");
    }

}
