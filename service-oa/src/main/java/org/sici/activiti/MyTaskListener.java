package org.sici.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @projectName: oa-parent
 * @package: org.sici.activiti
 * @className: MyTaskListener
 * @author: 749291
 * @description: TODO
 * @date: 3/21/2024 5:51 PM
 * @version: 1.0
 */

public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getName().equals("经理审批")) {
            delegateTask.setAssignee("Jack");
        }
        else if (delegateTask.getName().equals("人事审批")) {
            delegateTask.setAssignee("Lucy");
        }
    }
}
