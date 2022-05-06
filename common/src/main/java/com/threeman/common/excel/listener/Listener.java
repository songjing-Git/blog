package com.threeman.common.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/19 10:00
 */
@Slf4j
public class Listener<T> extends AnalysisEventListener<T> {

    /**
     * @param t               每次读取的数据
     * @param analysisContext 上下文
     */
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        log.info("t:{}", t);
    }

    /**
     * 读取文档完之后调用的方法
     *
     * @param analysisContext 上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
