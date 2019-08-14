package com.example.demo.service;

import com.example.demo.configs.exception.ValidateException;
import com.example.demo.configs.mapper.entity.Example;
import com.example.demo.constant.DrawFrequencyEnum;
import com.example.demo.controller.vo.DrawInfoVO;
import com.example.demo.controller.vo.DrawItemVO;
import com.example.demo.dao.ActivityTimeMapper;
import com.example.demo.dao.DrawCountConfigMapper;
import com.example.demo.dao.UserDrawRecordMapper;
import com.example.demo.model.ActivityTimeDO;
import com.example.demo.model.DrawCountConfigDO;
import com.example.demo.model.UserDrawRecordDO;
import com.example.demo.utils.DateUtil;
import com.example.demo.utils.RedisTemplateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    public static final String DRAW_COUNT_CONFIG_PREFIX = "draw_count_config_";
    @Autowired
    private DrawCountConfigMapper drawCountConfigMapper;

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
        // 判断活动时间
        ActivityTimeDO activityTimeDO = checkActivityTime(activityType);
        // 抽奖次数获取配置
        List<DrawCountConfigDO> configList = queryDrawCountConfigs(activityTimeDO);
        if (CollectionUtils.isEmpty(configList)) {
            return drawInfoVO;
        }

        List<UserDrawRecordDO> userDrawRecords = new ArrayList<>();
        Date now = new Date();
        // TODO 优化
        for (DrawCountConfigDO config : configList) {
            if (DrawFrequencyEnum.EVERYDAY.getCode().equals(config.getFrequency())) {
                Date startTime = DateUtil.getCurrentDayStartTime(now);
                Date endTime = DateUtil.getCurrentDayEndTime(now);
                List<UserDrawRecordDO> records = queryUserDrawRecords(activityType, config.getDrawCode(), userId, startTime, endTime);
                if (!CollectionUtils.isEmpty(records)) {
                    userDrawRecords.addAll(records);
                }
            }
            if (DrawFrequencyEnum.EVERY_WEEK.getCode().equals(config.getFrequency())) {
                Date startTime = DateUtil.getCurrentWeekDayStartTime(now);
                Date endTime = DateUtil.getCurrentWeekDayEndTime(now);
                List<UserDrawRecordDO> records = queryUserDrawRecords(activityType, config.getDrawCode(), userId, startTime, endTime);
                if (!CollectionUtils.isEmpty(records)) {
                    userDrawRecords.addAll(records);
                }
            }
            if (DrawFrequencyEnum.EVERY_MONTH.getCode().equals(config.getFrequency())) {
                Date startTime = DateUtil.getCurrentMonthStartTime(now);
                Date endTime = DateUtil.getCurrentMonthEndTime(now);
                List<UserDrawRecordDO> records = queryUserDrawRecords(activityType, config.getDrawCode(), userId, startTime, endTime);
                if (!CollectionUtils.isEmpty(records)) {
                    userDrawRecords.addAll(records);
                }
            }
            if (DrawFrequencyEnum.ONLY_ONE.getCode().equals(config.getFrequency())) {
                List<UserDrawRecordDO> records = queryUserDrawRecords(activityType, config.getDrawCode(), userId, activityTimeDO.getStartTime(), now);
                if (!CollectionUtils.isEmpty(records)) {
                    userDrawRecords.addAll(records);
                }
            }

        }
        // TODO


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
     * @param drawCode     抽奖编码
     * @param userId       用户ID
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return java.util.List<com.example.demo.model.UserDrawRecordDO>
     * @author zhou.xy
     * @date 2019/8/14
     * @since 1.0
     */
    private List<UserDrawRecordDO> queryUserDrawRecords(Integer activityType, Integer drawCode, Long userId,
                                                        Date startTime, Date endTime) {
        Example example = new Example(UserDrawRecordDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId)
                .andEqualTo("activityType", activityType)
                .andEqualTo("drawCode", drawCode);
        if (startTime != null) {
            criteria.andGreaterThanOrEqualTo("createdon", startTime);
        }
        if (endTime != null) {
            criteria.andLessThanOrEqualTo("createdon", endTime);
        }
        example.setOrderByClause("level ASC");
        return userDrawRecordMapper.selectByExample(example);
    }

    /**
     * 查询用户某个活动下抽奖次数获取配置记录，按抽奖优先级升序
     *
     * @param activityTimeDO 活动信息
     * @return java.util.List<com.example.demo.model.DrawCountConfigDO>
     * @author zhou.xy
     * @date 2019/8/14
     * @since 1.0
     */
    private List<DrawCountConfigDO> queryDrawCountConfigs(ActivityTimeDO activityTimeDO) {
        String key = DRAW_COUNT_CONFIG_PREFIX + activityTimeDO.getType();
        List<DrawCountConfigDO> configs = RedisTemplateUtil.get(key);
        if (!CollectionUtils.isEmpty(configs)) {
            return configs;
        }
        Example example = new Example(UserDrawRecordDO.class);
        example.createCriteria().andEqualTo("activityType", activityTimeDO.getType());
        example.setOrderByClause("level ASC");
        configs = drawCountConfigMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(configs)) {
            RedisTemplateUtil.set(key, configs, activityTimeDO.getEndTime().getTime() - activityTimeDO.getStartTime().getTime());
        }
        return configs;
    }
}
