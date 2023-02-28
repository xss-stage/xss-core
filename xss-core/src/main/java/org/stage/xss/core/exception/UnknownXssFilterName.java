package org.stage.xss.core.exception;


public final class UnknownXssFilterName extends RuntimeException{

    public UnknownXssFilterName(String name){
        super("Unknown xss filter name \"" + name + "\"");
    }

}
