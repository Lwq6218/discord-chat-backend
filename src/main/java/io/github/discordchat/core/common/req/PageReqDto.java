package io.github.discordchat.core.common.req;


import lombok.Data;

/**
 * @classname PageReqDto
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Data
public class PageReqDto {
    /**
     * 请求页码，默认第 1 页
     */
    private int pageNum = 1;

    /**
     * 每页大小，默认每页 10 条
     */
    private int pageSize = 2;

    /**
     * 是否查询所有，默认不查所有
     * 为 true 时，pageNum 和 pageSize 无效
     */

    private boolean fetchAll = false;

    private String filter = "recommended";

    private String q;
}
