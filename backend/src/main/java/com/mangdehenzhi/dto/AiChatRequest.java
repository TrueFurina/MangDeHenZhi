package com.mangdehenzhi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AiChatRequest {

    @Valid
    @NotNull(message = "对话消息不能为空")
    @NotEmpty(message = "对话消息不能为空")
    private List<AiChatMessage> messages;

    @Data
    public static class AiChatMessage {
        @jakarta.validation.constraints.NotBlank(message = "消息角色不能为空")
        private String role;

        @jakarta.validation.constraints.NotBlank(message = "消息内容不能为空")
        private String content;
    }
}
