package com.example.demo.service;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.Example;
import com.example.demo.controller.vo.DrawInfoVO;
import com.example.demo.controller.vo.DrawItemVO;
import com.example.demo.dao.ActivityTimeMapper;
import com.example.demo.dao.UserDrawRecordMapper;
import com.example.demo.model.ActivityTimeDO;
import com.example.demo.model.UserDrawRecordDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 幸运抽奖服务
 *
 * @author zhou.xy
 * @date 2019/8/14
 * @since 1.0
 */
@Log4j2
@Service
public class LuckDrawService {
    @Autowired
    private ActivityTimeMapper activityTimeMapper;
    @Autowired
    private UserDrawRecordMapper userDrawRecordMapper;

    /**
     * 判断活动是否开始
     *
     * @param activityType 活动类型
     * @return com.example.demo.model.ActivityTimeDO
     * @author zhou.xy
     * @date 2019/8/14
     * @since 1.0
     */
    public ActivityTimeDO checkActivityTime(Integer activityType) {
        if (activityType == null) {
            throw ValidateException.of("未知活动");
        }
        ActivityTimeDO activityTimeDO = new ActivityTimeDO();
        activityTimeDO.setType(activityType);
        activityTimeDO = activityTimeMapper.selectOne(activityTimeDO);
        if (activityTimeDO == null) {
            throw ValidateException.of("活动不存在！");
        }
        long nowTime = System.currentTimeMillis();
        if (nowTime < activityTimeDO.getStartTime().getTime()) {
            throw ValidateException.of("活动暂未开始，敬请期待！");
        }
        if (nowTime > activityTimeDO.getEndTime().getTime()) {
            throw ValidateException.of("很抱歉 ，活动已结束！");
        }
        return activityTimeDO;
    }


    /**
     * 获取可抽奖次数信息，不会返回null
     *
     * @param activityType 活动类型
     * @param userId       用户id
     * @return com.example.demo.controller.vo.DrawInfoVO
     * @author zhou.xy
     * @date 2019/8/14
     * @since 1.0
     */
    public DrawInfoVO queryUserDrawCountInfo(Integer activityType, Long userId) {
        DrawInfoVO drawInfoVO = new DrawInfoVO();
        drawInfoVO.setDrawCount(0);
        drawInfoVO.setDrawItemList(Collections.emptyList());
        if (userId == null) {
            return drawInfoVO;
        }
        checkActivityTime(activityType);

        List<UserDrawRecordDO> userDrawRecords = queryUserDrawRecords(activityType, userId);
        if (CollectionUtils.isEmpty(userDrawRecords)) {
            return drawInfoVO;
        }

        Integer drawCount = 0;
        List<DrawItemVO> drawItems = new ArrayList<>();
        for (UserDrawRecordDO record : userDrawRecords) {
            DrawItemVO item = new DrawItemVO();
            item.setAvailable(false);
            item.setDrawCode(record.getDrawCode());
            item.setDesc(record.getDrawDesc());
            if (UserDrawRecordDO.DRAW_NO.equals(record.getIfDraw())) {
                item.setAvailable(true);
                drawCount += 1;
            }
            drawItems.add(item);
        }

        if (drawCount > 0) {
            drawInfoVO.setDrawCount(drawCount);
            drawInfoVO.setDrawItemList(drawItems);
        }
        return drawInfoVO;
    }

    /**
     * 查询用户某个活动下抽奖记录（包含已抽和未抽的），按抽奖优先级升序
     *
     * @param activityType 活动类型
     * @param userId       用户ID
     * @return java.util.List<com.example.demo.model.UserDrawRecordDO>
     * @author zhou.xy
     * @date 2019/8/14
     * @since 1.0
     */
    private List<UserDrawRecordDO> queryUserDrawRecords(Integer activityType, Long userId) {
        Example example = new Example(UserDrawRecordDO.class);
        example.createCriteria().andEqualTo("userId", userId)
                .andEqualTo("activityType", activityType);
        example.setOrderByClause("level ASC");
        return userDrawRecordMapper.selectByExample(example);
    }
}
