package com.sunjet.backend.utils.activiti.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/21.
 */
@Component("customServiceTask")
public class CustomServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("CustomServiceTask");
    }
}
