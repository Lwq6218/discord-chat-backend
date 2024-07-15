package io.github.discordchat.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lwq
 * @since 2024/07/10
 */
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送者ID
     */
    private Long memberOne;

    /**
     * 接收者ID
     */
    private Long memberTwo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberOne() {
        return memberOne;
    }

    public void setMemberOne(Long memberOne) {
        this.memberOne = memberOne;
    }

    public Long getMemberTwo() {
        return memberTwo;
    }

    public void setMemberTwo(Long memberTwo) {
        this.memberTwo = memberTwo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Conversation{" +
        "id=" + id +
        ", memberOne=" + memberOne +
        ", memberTwo=" + memberTwo +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
