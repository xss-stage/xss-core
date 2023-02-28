# xss-stage
xss-stage는 다양한 xss 라이브러리를 통합하고 쉽게 적용하기 위해 탄생한 라이브러리 입니다.   
xss-core는 자유롭게 XssFilter를 등록하고 확장하여 사용할 수 있으며, 요청단에서 xss 필터링을 진행하기 때문에, 응답과정에서 필터링을 진행하는 방식보다 특정 상황(응답이 요청보다 많은 상황)에서 더 효율적일 수 있습니다.   

[![made with love](https://camo.githubusercontent.com/c6c5b56fc051557203c6dffa4242b41b09ff22f6303da15e47162a5c1691e8a5/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4d616465253230776974682d4c6f76652d2d2545322539442541342d726564)](https://camo.githubusercontent.com/c6c5b56fc051557203c6dffa4242b41b09ff22f6303da15e47162a5c1691e8a5/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4d616465253230776974682d4c6f76652d2d2545322539442541342d726564)  
![needed jdk version](https://img.shields.io/badge/JDK-8-blue)   
![api-version](https://img.shields.io/badge/xss--core-1.1-3F9DE5)  ![api-version](https://img.shields.io/badge/xss--extension--string-1.1-92CE64)  ![api-version](https://img.shields.io/badge/xss--extension--json-1.1-F29494)

## Extensions
core : [xss-core-1.1](https://github.com/xss-stage/xss-core)    
string-extension : [xss-extension-string-1.1](https://github.com/xss-stage/xss-extension-string)   
json-extension : [xss-extension-json-1.1](https://github.com/xss-stage/xss-extension-json)   

## Downloads
다운로드에 관한 자세한 내용은 [여기](https://github.com/xss-stage)를 참조해주세요.

## Usage
xss-stage에서 사용할 수 있는 인터페이스와 확장법은 모두 xss-core 라이브러리에 작성되어 있습니다.   
Custom Xss Filter가 필요한 상황이 필요한 상황이 아니라면, 다음 인터페이스로 Xss-filtering을 진행할 수 있습니다.   
만약 Custom Xss Filter가 필요하다면, 이 다음 [목차](Extension)를 참고하세요.
   
> `@XssFiltering` : 메소드에 마킹가능하며, 마킹된 메소드는 XssFiltering의 대상이 됩니다.   
> `@Xss` : `@XssFiltering`이 마킹된 메소드의 파라미터에 마킹가능하며, 마킹된 파라미터를 대상으로 Xss filtering이 진행됩니다. 
> `@Xss`는 String filterName() 메소드를 갖고있으며, 이 메소드에 값을 설정하는것으로 파라미터를 필터링할때 사용할 XssFilter를 결정할 수 있습니다.
> filterName()은 value()와 동일하며, 이 둘 모두 생략된다면, 파라미터의 클래스명을 모두 소문자로 변경한 값으로 XssFilter를 결정합니다.
   
다음은 실제 사용예시 입니다.

``` Java

@RestController
public class Example{
  
    @XssFiltering // 이 어노테이션이 마킹된 메소드는 XssFiltering의 대상이 됩니다.
    @GetMapping("/example")
    public Object helloworld(@Xss String param1, @Xss("json") SomeObject param2, @Xss("string") String param3, String param4){
        // @XssFiltering 어노테이션이 마킹된 메소드의 파라미터에 @Xss 어노테이션을 마킹함으로써 Xss safe한 객체를 얻을 수 있습니다.
        // @Xss의 value()에 어떠한 값도 들어가지 않는다면, 마킹된 파라미터의 클래스 이름을 모두 소문자로 변경한 값이 됩니다.
        // @Xss의 value()에 값을 넣음으로써, 이 파라미터를 필터링 하는데 사용할 XssFilter 구현체를 선택할 수 있습니다.
        // @Xss에 들어갈 수 있는 값은 xss-extension 레포지토리를 참고하세요.
        ...
    }
  
}

```

## Extension
XssCore에 CustomFilter는 다음과 같은 과정을 통해 등록할 수 있습니다.
1. org.stage.xss.core.spi.XssFilter 인터페이스를 구현한다.
2. 해당 인터페이스를 스프링 빈으로 등록시킨다.

다음은 XssFilter인터페이스 입니다.
``` Java
package org.stage.xss.core.spi;

/**
 * 이 인터페이스의 구현체는 파라미터를 전달받아 Xss-safe 한 응답을 반환해야함.
 *
 * @since 0.1
 * @author devxb
 */
public interface XssFilter{

    /**
     * XssFilter 구현체의 이름을 반환함. <br>
     * 이 이름은 Xss Filtering 대상을 식별하는데 사용됨. {@link org.stage.xss.core.meta.Xss} <br>
     *
     * @return String XssFilter 구현체의 의 이름
     */
    String getFilterName();

    /**
     * 파라미터 'dirty' 와 'cast' 를 인자로 받고, <br>
     * 'dirty' 를 Xss-safe 한 상태로 필터링 한 결과를 'cast' 타입으로 반환함.
     *
     * @param dirty Xss-safe 한 상태로 필터링 할 대상.
     * @param cast Xss-safe 한 객체가 반환될 타입.
     * @return Xss-safe 한 객체
     * @param <P> 반환 될 타입
     */
    <P> P doFilter(Object dirty, Class<P> cast);

}

```
getFilterName()은 `@Xss(value = ?)`의 ? 와 매칭되며, xss-core는 ?에 해당하는 반환값은 갖고있는 XssFilter를 찾아 filtering을 진행합니다.


