package com.fgrapp.base.result;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * ResponseResultBody
 *
 * @author fan guang rui
 * @date 2021年06月09日 13:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ResponseBody
public @interface ResponseResultBody {

}
