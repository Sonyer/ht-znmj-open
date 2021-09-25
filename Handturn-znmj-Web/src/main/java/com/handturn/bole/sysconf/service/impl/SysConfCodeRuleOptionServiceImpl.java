package com.handturn.bole.sysconf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.handturn.bole.sysconf.dto.CodeRuleGenerateDealDTO;
import com.handturn.bole.sysconf.entity.SysconfCodeRule;
import com.handturn.bole.sysconf.mapper.SysconfCodeRuleMapper;
import com.handturn.bole.sysconf.service.CodeRuleDealSynLock;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleOptionService;
import com.handturn.bole.sysconf.service.ISysconfCodeRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;

/**
 * 编码生成 Service实现
 *
 * @author Eric
 * @date 2020-01-12 09:42:35
 */
@Service("SysconfCodeRuleOptionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysConfCodeRuleOptionServiceImpl extends ServiceImpl<SysconfCodeRuleMapper, SysconfCodeRule> implements ISysconfCodeRuleOptionService {

    @Autowired
    private ISysconfCodeRuleService sysconfCodeRuleService;


    /**
     * 是否完成处理 -线程锁
     * @param codeRuleGenerateDealDTO
     * @return
     */
    public String generateCode(CodeRuleGenerateDealDTO codeRuleGenerateDealDTO){
        return sysconfCodeRuleService.generateCode(codeRuleGenerateDealDTO.getRuleCode(),codeRuleGenerateDealDTO.getParams());
    }

    /**
     * 是否完成处理 -线程锁
     * @param codeRuleGenerateDealDTO
     * @return
     */
    @Override
    @Transactional
    public String generateCodeMain(CodeRuleGenerateDealDTO codeRuleGenerateDealDTO){
        final CodeRuleDealSynLock<String> vsl= new CodeRuleDealSynLock<String>(){
            public Object run(){
                /************ 你要进行同步的代码块********************/
                try {
                    String code = generateCode((CodeRuleGenerateDealDTO) this.paramObj);
                    return code;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                /********************************/
            }};
        ExecutorService service= Executors.newCachedThreadPool();

        //同步返回
        FutureTask<Object> callableTask = (FutureTask<Object>) service.submit(new Callable<Object>() {
            @Override
            public Object call() throws InterruptedException {
                Object result = vsl.startWork(codeRuleGenerateDealDTO.getRuleCode(),codeRuleGenerateDealDTO); //startWork(参数值  相同就会同步【根据值进行同步】)
                return result;
            }
        });

        try {
            return (String)callableTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
