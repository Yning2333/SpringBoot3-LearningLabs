package com.example.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class LeaveFailService implements JavaDelegate{
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("审批不通过" + execution.getCurrentActivityId());
    }
}
