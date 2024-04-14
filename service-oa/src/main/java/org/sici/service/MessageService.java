package org.sici.service;

public interface MessageService {
    /**
     * 推送待审批人员
     * @param processId
     * @param userId
     * @param taskId
     */
    void pushPendingMessage(Long processId, Long userId, String taskId);

    void pushProcessedMessage(Long processId, Long userId, Integer status);
}
