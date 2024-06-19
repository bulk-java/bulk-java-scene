package org.slf4j.impl;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import top.bulk.mdc.adapter.TtlMDCAdapter;

/**
 * 重写 slf4j 的 MDC 获取逻辑。包名类名必须完全一样才能加载到自己的 StaticMDCBinder
 * <p>
 * 然后重写 getMDCA 的返回值，即可实现使用自己的 MDC
 *
 * @author shixiaodong
 * @date 2024-06-19
 */
@SuppressWarnings("all")
public class StaticMDCBinder {
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    /**
     * 重写 MDC 获取逻辑，获取的是自己的
     *
     * @return TtlMDCAdapter
     */
    public MDCAdapter getMDCA() {
        return new TtlMDCAdapter();
        // return new LogbackMDCAdapter();
    }

    public String getMDCAdapterClassStr() {
        return LogbackMDCAdapter.class.getName();
    }
}
