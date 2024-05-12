package io.github.hubao.hbconfig.server;

import io.github.hubao.hbconfig.server.mapper.LocksMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Desc:
 *
 * @author hubao
 * @see 2024/5/12 20:59
 */
@Component
@Slf4j
public class DistributedLocks {

    @Autowired
    private LocksMapper locksMapper;

    @Autowired
    DataSource dataSource;

    Connection connection;

    @Getter
    private AtomicBoolean locked = new AtomicBoolean(false);

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    @PostConstruct
    public void init() {

        try {
            connection = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        executorService.scheduleWithFixedDelay(this::tryLock, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    public boolean lock() throws Exception {

        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.createStatement().execute("set innodb_lock_wait_timeout=5");
        connection.createStatement().execute("select app from locks where 1=1 and id=1 for update");

        if (locked.get()) {
            log.info("reenter this dist lock.");
        }else {
            log.info("get a dist lock");
        }


        return true;
    }

    private void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception e) {
            log.error("lock failed...");
            locked.set(false);
        }
    }

    @PreDestroy
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (Exception e) {
            log.error("ignore this exception");
        }
    }
}
