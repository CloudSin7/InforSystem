package com.sin.inforsystembackend.util;

import com.sin.inforsystembackend.dto.NotificationDTO;
import com.sin.inforsystembackend.entity.NotificationIndex;

import java.util.List;
import java.util.stream.Collectors;

public class DtoConverter {

    /**
     * 将 NotificationIndex 转换为 NotificationDTO
     * @param index NotificationIndex 实体
     * @return NotificationDTO
     */
    public static NotificationDTO convertToNotificationDTO(NotificationIndex index) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(index.getNotification().getNotificationId());
        dto.setTitle(index.getNotification().getTitle());
        dto.setContent(index.getNotification().getContent());
        dto.setType(index.getNotification().getNotificationType().name());
        dto.setSenderDepartment(index.getNotification().getSenderDepartment().getDepartmentName());
        dto.setSentTime(index.getNotification().getSentTime());
        dto.setIsRead(index.isReadStatus());
        return dto;
    }

    /**
     * 将 List<NotificationIndex> 转换为 List<NotificationDTO>
     * @param indexes NotificationIndex 列表
     * @return NotificationDTO 列表
     */
    public static List<NotificationDTO> convertToNotificationDTOList(List<NotificationIndex> indexes) {
        return indexes.stream()
                .map(DtoConverter::convertToNotificationDTO) // 复用单个转换逻辑
                .collect(Collectors.toList());
    }
}