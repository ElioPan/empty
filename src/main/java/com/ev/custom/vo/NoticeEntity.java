package com.ev.custom.vo;

import com.ev.custom.domain.NoticeDO;
import com.ev.custom.domain.NoticeTypeDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xy
 * @Date 2020/3/27 15:21
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private NoticeDO noticeDO;

    private NoticeTypeDO noticeTypeDO;

    private List<Long> toUserList;
}
