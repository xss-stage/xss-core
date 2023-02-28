package org.stage.xss.core;

import org.stage.xss.core.meta.Xss;
import org.stage.xss.core.meta.XssFiltering;

public class XssFilterTarget{

    @XssFiltering
    public String filteringString(@Xss("string") String hello){
        return hello;
    }

    @XssFiltering
    public String filteringTwoParameter(@Xss("string") String hello, @Xss String world){
        return hello + world;
    }

    @XssFiltering
    public String filteringOneParameter(@Xss String hello, String world){
        return hello + world;
    }

    @XssFiltering
    public String filterWrongXssAnnotation(@Xss("wrong") String hello){
        return hello;
    }

}
