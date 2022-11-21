package com.atguigu.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ExcelDictDTOListener
 * @Description TODO
 * @Author mch
 * @Date 2022/11/15
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {
    public static final int BATCH_COUNT = 5;
    List<ExcelDictDTO> list = new ArrayList<>();
    private DictMapper dictMapper;

    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

/**
 * @Author mch
 * @Description //TODO 遍历每一行的记录
 * @Date 2022/11/15
 * @Param [data, context]
 * @return void
 **/
    @Override
    public void invoke(ExcelDictDTO data, AnalysisContext context) {
        log.info("解析到一条记录:{}",data);
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    private void saveData() {
        log.info("{}条数据,开始存入数据库!",list.size());
        dictMapper.insertBatch(list);
        log.info("存储数据库成功!");
    }
    //所有数据解析完成时调用
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        log.info("所有数据解析完成!");
    }
}
