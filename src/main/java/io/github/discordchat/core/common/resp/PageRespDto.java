package io.github.discordchat.core.common.resp;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;

import java.util.List;

/**
 * @classname PageRespDto
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Getter
public class PageRespDto<T> {
    /**
     * 页码
     */
    @Parameter(description = "页码")
    private final long pageNum;

    /**
     * 每页大小
     */
    @Parameter(description = "每页大小")
    private final long pageSize;

    /**
     * 总记录数
     */
    @Parameter(description = "总记录数")
    private final long total;


      /**
     * 分页数据集
     */
    @Parameter(description = "分页数据集")
    private final List<? extends T> list;

    /**
     * 该构造函数用于通用分页查询的场景
     * 接收普通分页数据和普通集合
     */
    public PageRespDto(long pageNum, long pageSize, long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    public static <T> PageRespDto<T> of(long pageNum, long pageSize, long total, List<T> list) {
        return new PageRespDto<>(pageNum, pageSize, total, list);
    }

    /**
     * 获取分页数
     * */
    public long getPages() {
        if (this.pageSize == 0L) {
            return 0L;
        } else {
            long pages = this.total / this.pageSize;
            if (this.total % this.pageSize != 0L) {
                ++pages;
            }

            return pages;
        }
    }
}
