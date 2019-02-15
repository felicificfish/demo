package com.example.demo.test;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.GiftDO;
import com.example.demo.utils.LotteryUtil;
import com.example.demo.utils.NumberToCN;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 测试
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
public class MyTest {
    private static Pattern numberPattern = Pattern.compile("[0-9]+");

    public static void main(String[] args) {
//        String str = "data:image/png;base64,iVBORw0KGgoAAAANSU";
//        String str2 = str.replaceFirst("data:image/\\S*;base64,", "");
//        log.info(str);
//        log.info(str2);
//
//        Long a = 123L;
//        String b = "123";
//        log.info(b.equals(a.toString()));
//        log.info(Objects.equals(a, b));
//
//        log.info(OptTypeEnum.getByCode("BIND2".replaceAll("\\d+", "")).getMsg());
//
//        log.info(numberPattern.matcher("BIND2").replaceAll(""));
//
//        lottery();

//        doBatch();

        System.out.println(NumberToCN.number2CNMontrayUnit(BigDecimal.valueOf(12323324.2323)));
        System.out.println(NumberToCN.formatCNDecimal(BigDecimal.valueOf(12323324.2323)));
    }

    public enum OptTypeEnum {
        OPT_UNBIND("UNBIND", "您已存在解绑操作"),
        OPT_BIND("BIND", "您已存在绑卡操作"),
        OPT_CHANGE("CHANGE", "您已存在换卡操作");
        private String code;
        private String msg;

        OptTypeEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static OptTypeEnum getByCode(String code) {
            for (OptTypeEnum aCode : OptTypeEnum.values()) {
                if (aCode.getCode().equals(code)) {
                    return aCode;
                }
            }
            return null;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

    private static void lottery() {
        GiftDO iphone = new GiftDO();
        iphone.setId(101);
        iphone.setName("苹果手机");
        iphone.setProbability(0.1D);

        GiftDO thanks = new GiftDO();
        thanks.setId(102);
        thanks.setName("再接再厉");
        thanks.setProbability(0.5D);

        GiftDO vip = new GiftDO();
        vip.setId(103);
        vip.setName("优酷会员");
        vip.setProbability(0.4D);

        List<GiftDO> list = new ArrayList<GiftDO>();
        list.add(vip);
        list.add(thanks);
        list.add(iphone);

        for (int i = 0; i < 100; i++) {
            int index = LotteryUtil.lottery(list);
            log.info(JSON.toJSONString(list.get(index)));
        }
    }

    private static void doBatch() {
        List<String> dataList = new ArrayList<>();
        dataList.add("11");
        dataList.add("22");
        dataList.add("33");
        dataList.add("44");
        dataList.add("55");
        dataList.add("66");
        dataList.add("77");
        dataList.add("88");
        dataList.add("99");
        dataList.add("00");
        dataList.add("01");
        dataList.add("02");
        dataList.add("03");
        dataList.add("04");

        // 分批插入数据库
        int limitSize = 4;
        int size = dataList.size();
//        if (size > limitSize) {
//            int batch = size / limitSize;
//            for (int i = 0; i < batch; i++) {
//                System.out.println("第" + (i + 1) + "批：" + dataList.subList(0, limitSize));
//                dataList.subList(0, limitSize).clear();
//            }
//        }
//        if (!dataList.isEmpty()) {
//            System.out.println("剩下的：" + dataList);
//        }

        int batchNos = size % limitSize == 0 ? size / limitSize : (size / limitSize) + 1;
        Map<String, List<String>> datas = new HashMap<>();
        int startIndex = 0;
        int stopIndex = 0;
        for (int i = 0; i < batchNos; i++) {
            stopIndex = (i == batchNos - 1) ? size : stopIndex + limitSize;
            List<String> tempList = new ArrayList<>(dataList.subList(startIndex, stopIndex));
            datas.put(String.valueOf(i), tempList);
            startIndex = stopIndex;
        }
        System.out.println(datas);
    }
}
