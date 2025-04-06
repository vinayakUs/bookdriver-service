package org.example.driverservice.redlock;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.SetArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class RedLock {

    private static final String LOCK_MSG = "OK";
    private static final Long UNLOCK_MSG = 1L;
    private final String lockPrefix = "lock_";
    /*
    * Time Ms
     */
    private static final int TIME = 30000;
    private static final int OPERATION_TIMEOUT_MS = 30000;


    @Autowired
    private GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;


    /**
     * Blocking lock with maximum wait time
     *
     * @param key Lock key
     * @param request Unique request ID
     * @param blockTime Maximum time to wait for lock (milliseconds)
     * @return true if lock acquired, false if timeout
     */
    public boolean lock(String key, String request, long blockTime) {
        long startTime = System.currentTimeMillis();
        int attempts = 0;

        while (System.currentTimeMillis() - startTime <= blockTime) {
            attempts++;
            StatefulRedisModulesConnection<String, String> connection = null;

            try {
                connection = pool.borrowObject();

                // Validate connection
                if (!validateConnection(connection)) {
                    pool.invalidateObject(connection);
                    connection = null;
                    continue;
                }

                // Try to acquire lock
                String res = connection.sync()
                        .set(lockPrefix + key, request, SetArgs.Builder.nx().px(TIME));

                if (LOCK_MSG.equals(res)) {
                    log.debug("Lock acquired for {} after {} attempts", key, attempts);
                    return true;
                }

                // Calculate adaptive wait time
                long sleepTime = calculateBackoff(attempts);
                Thread.sleep(sleepTime);

            } catch (TimeoutException e) {
                log.warn("Connection timeout for {}", key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Lock interrupted for {}", key);
                return false;
            } catch (Exception e) {
                log.error("Error acquiring lock for {}", key, e);
            } finally {
                if (connection != null) {
                    try {
                        pool.returnObject(connection);
                    } catch (Exception e) {
                        log.warn("Error returning connection", e);
                    }
                }
            }
        }

        log.warn("Failed to acquire lock for {} after {}ms", key, blockTime);
        return false;
    }

    private long calculateBackoff(int attempt) {
        // Exponential backoff with jitter
        long base = Math.min(
                100 * (1L << Math.min(attempt, 10)), // Exponential growth (max 102.4s)
                10_000 // Cap at 10 seconds
        );
        return base + (long)(Math.random() * 100); // Add jitter
    }



    private boolean validateConnection(StatefulRedisModulesConnection<String, String> connection){
        try {
            return "PONG".equals(connection.sync().ping());
        }catch (Exception e){
            return false;
        }
    }



    public void tryLock(String key, String request) {
        try (StatefulRedisModulesConnection<String, String> connection = pool.borrowObject()) {
            for (; ; ) {
                String res = connection.async().set(lockPrefix + key, request, new SetArgs().px(TIME).nx()).get();
                System.out.println(res);

                if (LOCK_MSG.equals(res)) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
