package com.example.demo.utils;

import com.example.demo.model.SensitiveWordDO;
import com.example.demo.service.SensitiveWordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 敏感词初始化
 *
 * @author zhou.xy
 * @date 2019/9/10
 * @since 1.0
 */
@Log4j2
public class SensitiveWordInit {
    public static final String SENSITIVE_WORD_CACHE_KEY = "sensitive_word_map_dfa";
    public HashMap<String, Object> sensitiveWordMap;
    @Autowired
    private SensitiveWordService sensitiveWordService;

    /**
     * 初始化敏感词
     *
     * @param
     * @return java.util.Map
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    public Map<String, Object> initSensitiveWord() {
        sensitiveWordMap = RedisTemplateUtil.get(SENSITIVE_WORD_CACHE_KEY);
        if (CollectionUtils.isEmpty(sensitiveWordMap)) {
            if (sensitiveWordService == null) {
                sensitiveWordService = SpringContext.getBean(SensitiveWordService.class);
            }
            List<SensitiveWordDO> sensitiveWordList = sensitiveWordService.queryList();
            if (CollectionUtils.isEmpty(sensitiveWordList)) {
                return null;
            }
            Set<String> keywordSet = new HashSet<>();
            for (SensitiveWordDO wordDO : sensitiveWordList) {
                keywordSet.add(wordDO.getWord().trim());
            }
            // 将敏感词库加到HashMap
            addSensitiveWordToHashMap(keywordSet);
            RedisTemplateUtil.set(SENSITIVE_WORD_CACHE_KEY, sensitiveWordMap);
        }
        return sensitiveWordMap;
    }

    /**
     * 将敏感词库加到HashMap
     *
     * @param keywordSet
     * @return void
     * @author zhou.xy
     * @date 2019/9/10
     * @since 1.0
     */
    private void addSensitiveWordToHashMap(Set<String> keywordSet) {
        sensitiveWordMap = new HashMap<>(keywordSet.size());
        // 敏感词
        String word = null;
        /*
         * 等于敏感词库，HashMap对象在内存中占用的是同一个地址，currentMap对象发生变化，
         * sensitiveWordMap对象也会跟着改变
         */
        Map currentMap = null;
        Map<String, String> newWordMap = null;
        Iterator<String> iterator = keywordSet.iterator();
        while (iterator.hasNext()) {
            word = iterator.next();
            currentMap = sensitiveWordMap;
            for (int i = 0; i < word.length(); i++) {
                // 截取敏感词当中的字，在敏感词库中字为HashMap的key
                char wordChar = word.charAt(i);
                Object o = currentMap.get(wordChar);
                if (o != null) {
                    currentMap = (Map) o;
                } else {
                    newWordMap = new HashMap<>();
                    newWordMap.put("isEnd", "0");
                    currentMap.put(wordChar, newWordMap);
                    currentMap = newWordMap;
                }
                if (i == word.length() - 1) {
                    currentMap.put("isEnd", "1");
                }
            }
        }
    }
}
