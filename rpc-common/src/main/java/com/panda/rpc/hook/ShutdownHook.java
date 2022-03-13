package com.panda.rpc.hook;

import com.panda.rpc.util.NacosUtil;
import com.panda.rpc.util.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/3/9 3:25 下午
 */

public class ShutdownHook {
    public static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    public static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }


    public void addClearAllHook() {
        logger.info("服务端关闭前需要注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            ThreadPoolFactory.shutDownAll();
        }));
    }
}
