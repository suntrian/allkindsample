package org.example.springSample.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component("arithmeticAspect")
public class ArithmeticAspect {

    /**
     * 前置通知 方法开始之前执行
     * @param joinPoint
     */
    @Before("execution(public Integer com.suntr.springSample.aop.ArithmeticImpl.*(Integer, Integer))")
    public void beforeArithmetic(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("Method:" + methodName + "begins with args:" + args);
    }

    /**
     * 后置通知，方法执行完之后执行，不论方法是否出现异常
     * 后置通知中不能访问目标方法的执行结果
     */
    @After("execution(public Integer com.suntr.springSample.aop.ArithmeticImpl.*(Integer, Integer))")
    public void afterArithmetic(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The method " + methodName + " ends with " + args);
    }
    /**
     * 返回通知，在方法正常结束之后执行的代码
     * 返回通知可以访问方法的返回值
     */
    @AfterReturning(value = "execution(public Integer com.suntr.springSample.aop.ArithmeticImpl.*(Integer, Integer))", returning = "result")
    public void afterReturnArithmetic(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Method :" + methodName + " returns:" + result);
    }
    /**
     * 异常通知:在方法抛出异常之后执行
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "execution(public Integer com.suntr.springSample.aop.ArithmeticImpl.*(Integer,Integer))", throwing = "e")
    public void AfterThrowingArithmetic(JoinPoint joinPoint, Exception e) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName + e.getMessage());
    }

    //@Around(value = "execution(public Integer com.suntr.springSample.aop.ArithmeticImpl.*(Integer, Integer))")
    public Object arroundArithmetic(ProceedingJoinPoint joinPoint)  throws Throwable{
        String methodName = joinPoint.getSignature().getName();
        Object result = null;
        long beforeTime = 0;
        long finishTime = 0;
        try {
            beforeTime = System.nanoTime();
            System.out.println("start at:" + beforeTime);
            result = joinPoint.proceed();
            finishTime = System.nanoTime();
            System.out.println("end at:" + finishTime);
        }  finally {
            System.out.println("nanoSec takes:" +  (finishTime - beforeTime));
        }

        return result;
    }
}
