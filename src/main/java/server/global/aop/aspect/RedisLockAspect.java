package server.global.aop.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import server.global.aop.annotation.RedisLock;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RedisLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String lockNameTemplate = redisLock.lockName();
        String lockName = parseLockName(lockNameTemplate, joinPoint);

        RLock lock = redissonClient.getLock(lockName);
        try {
            boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (isLocked) {
                return joinPoint.proceed();
            } else {
                throw new ErrorHandler(ErrorStatus.FAILED_TO_ACQUIRE_LOCK);
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String parseLockName(String lockNameTemplate, ProceedingJoinPoint joinPoint) {
        // SpEL(Spring Expression Language)을 사용하여 lockNameTemplate에 있는 변수를 실제 값으로 치환
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();

        // 메서드의 파라미터를 가져와서 컨텍스트에 추가
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // lockNameTemplate을 파싱하여 실제 락 이름 생성
        String parsedLockName = parser.parseExpression('"' + lockNameTemplate + '"').getValue(context, String.class);
        return parsedLockName;
    }
}
