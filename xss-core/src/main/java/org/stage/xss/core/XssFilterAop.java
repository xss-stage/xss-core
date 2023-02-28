package org.stage.xss.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stage.xss.core.exception.UnknownXssFilterName;
import org.stage.xss.core.meta.Xss;
import org.stage.xss.core.spi.XssFilter;

@Aspect
@Component
public class XssFilterAop{

    private final List<XssFilter> xssFilters;

    @Autowired
    XssFilterAop(List<XssFilter> xssFilters){
        this.xssFilters = xssFilters;
    }

    @Around("@annotation(org.stage.xss.core.meta.XssFiltering)")
    public Object doFiltering(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Parameter[] parameters = extractParameters(proceedingJoinPoint);
        Object[] xssFilteredArgs = doXssFiltering(proceedingJoinPoint.getArgs(), parameters);
        return proceedingJoinPoint.proceed(xssFilteredArgs);
    }

    private Parameter[] extractParameters(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Method[] methods = targetClass.getMethods();
        for (Method method : methods){
            if (method.getName().equals(methodName)){
                return method.getParameters();
            }
        }
        throw new IllegalStateException("Can not find method named \"" + methodName + "\"");
    }

    private Object[] doXssFiltering(Object[] objects, Parameter[] parameters){
        for(int i = 0; i < parameters.length; i++){
            if(isAnnotatedXss(parameters[i])){
                objects[i] = doXssFiltering(objects[i], parameters[i]);
            }
        }
        return objects;
    }

    private boolean isAnnotatedXss(Parameter parameter){
        return parameter.getAnnotation(Xss.class) != null;
    }

    private Object doXssFiltering(Object object, Parameter parameter){
        String xssFilterName = getXssAnnotationValue(parameter);
        for(XssFilter xssFilter : xssFilters){
            if(xssFilterName.equals(xssFilter.getFilterName())){
                return xssFilter.doFilter(object, parameter.getType());
            }
        }
        throw new UnknownXssFilterName(xssFilterName);
    }

    private String getXssAnnotationValue(Parameter parameter){
        Xss xss = parameter.getAnnotation(Xss.class);
        if(!xss.value().equals("")){
            return xss.value();
        }
        if(!xss.filterName().equals("")){
            return xss.filterName();
        }
        return parameter.getType().getSimpleName().toLowerCase();
    }

}
