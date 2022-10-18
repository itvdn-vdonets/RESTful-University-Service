package com.university.api.service.aop;

import com.university.api.service.models.Student;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAopAspect {

    @Pointcut("execution(* com.university.api.service.services.*.*(..))")
    public void joinPointerServiceLog() {
    }

    @Pointcut("execution(* com.university.api.service.models.*.get*(..))")
    public void allGetters() {}

    @Pointcut("execution(* com.university.api.service.services.StudentService.add*(..)) && args(student)")
    public void addStudentPointerServiceLog(Student student) {
    }

    @Before("joinPointerServiceLog()")
    public void beforeAnyServiceMethod(JoinPoint joinPoint) {
        System.out.println("Имя таргет метода: " + joinPoint.getSignature().getName());
        System.out.println("Простое имя класса: " + joinPoint.getSignature().getDeclaringType().getSimpleName());
        System.out.println("Имя класса, которому принадлежит метод: " + joinPoint.getSignature().getDeclaringType());
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("Аргумент "+(i+1)+" с параметром: "+args[i]);
        }
        System.out.println("Проксируемый объект = " + joinPoint.getTarget());
        System.out.println("Реальный объект = " + joinPoint.getThis());
    }

    @AfterThrowing(pointcut = "joinPointerServiceLog()", throwing = "ex")
    public void logAfterThrowingAllMethods(IllegalArgumentException ex) {
        System.out.println("ERROR: " + ex);
    }

    @AfterReturning(pointcut = "joinPointerServiceLog()", returning = "value")
    public void logAfterReturningAllMethods(Object value) {
        System.out.println("Returing: "+value.toString());
    }

    @Around("addStudentPointerServiceLog(student)")
    public Object aroundAllMethods(ProceedingJoinPoint joinPoint, Student student) {
        Object result = null;
        try {
            System.out.println("Перед выполнением метода...");
            Student testUser = new Student();
            testUser.setName("MYPROXYNAME");
            result = joinPoint.proceed(new Object[] { testUser });
            System.out.println("После того, как метод вернул результат..." + result.toString());
        } catch (Throwable e) {
            System.out.println("Непредвиденная ситуация..."+e);
            throw new RuntimeException(e);
        }
        System.out.println("После выполнения...");
        return result;
    }


}
