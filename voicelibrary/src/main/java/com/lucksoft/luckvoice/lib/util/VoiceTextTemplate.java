package com.lucksoft.luckvoice.lib.util;

import static com.lucksoft.luckvoice.lib.util.VoiceCons.A;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.B;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.C;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.CHAIYOU;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.CHONGZHI;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.CNG;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.D;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.DIAN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.E;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.F;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.G;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.GONGJIN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.H;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.HAO;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.HAOQIANG;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.I;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.J;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.JIFENDIKOU;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.JIN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.K;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.L;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.LIFANG;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.LNG;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.M;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.N;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NIYOUYIBIXINDINGDAN;

import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_EIGHT;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_FIVE;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_FOUR;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_NINE;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_ONE;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_SEVEN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_SIX;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_THREE;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_TWO;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.NUMBER_ZERO;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.O;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.P;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.Q;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.QINGQUCAN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.QINGZHUNBEIJIUCAN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.QIYOU;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.R;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.S;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.SHENG;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.SHISHOU;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.SHOUKUAN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.SHOUYIN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.T;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.TIANRANQI;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.U;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.UNIT_HUNDRED;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.UNIT_HUNDREDMILLION;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.UNIT_TEN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.UNIT_TENTHOUSAND;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.UNIT_THOUSAND;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.V;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.W;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.WEIXIN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.X;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.XIANJIN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.Y;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.YINGSHOU;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.YINHANGKA;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.YOUHUI;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.YUAN;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.YUE;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.Z;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.ZHANGHU;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.ZHIFU;
import static com.lucksoft.luckvoice.lib.util.VoiceCons.ZHIFUBAO;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Time：2023/2/18
 * Author：feng
 * Description：
 */
public class VoiceTextTemplate {
    private static HashMap<String,String> voiceCharacterList;
    private static HashMap<String,Long> delayTimeList;//延迟时间

    public static HashMap<String, Long> getDelayTimeList() {
        return delayTimeList;
    }

    public static List<String> getVoiceList(ArrayList<String> inputList, boolean haveMoney) {
        initVoiceConsList();
        List<String> voiceList = new ArrayList<>();
        if (inputList == null) return voiceList;
        for (String input : inputList) {
            if (TextUtils.isEmpty(input)) continue;
            if (StringUtil.isNumber(input)) {
                //纯数字 是否开启金额播报模式
                if (haveMoney) {
                    voiceList.addAll(genReadableMoney(input));
                } else {
                    voiceList.addAll(createReadableNumList(input));
                }
            } else if (StringUtil.isLetter(input)) {
                //纯字母
                voiceList.addAll(getLetters(input));
            } else {
                //匹配voiceCharacterList
                checkCharacterList(input,voiceList,haveMoney);
            }
        }
        Log.d("VoiceTextTemplate", voiceList.toString());
        return voiceList;
    }

    private static void checkCharacterList(String s, List<String> voiceList, boolean haveMoney){
        for (int i = s.length() ;i > 0 ;i --){
            String substring = s.substring(0,i);
            Log.d("VoiceTextTemplate", "分割匹配音频："+substring);
            if (StringUtil.isNumber(substring)){
                Log.d("VoiceTextTemplate", "匹配数字成功："+substring);
                //开启金额播报需要组合数字
                if (haveMoney) {
                    voiceList.addAll(genReadableMoney(substring));
                } else {
                    voiceList.addAll(createReadableNumList(substring));
                }
                if (!substring.equals(s)){
                    checkCharacterList(s.substring(i),voiceList, haveMoney);
                }
                return;
            }else if (voiceCharacterList.containsKey(substring) ){
                Log.d("VoiceTextTemplate", "匹配成功："+substring);
                voiceList.add(voiceCharacterList.get(substring));
                if (!substring.equals(s)){
                    checkCharacterList(s.substring(i),voiceList, haveMoney);
                }
                return;
            } else if (voiceCharacterList.containsValue(substring)){
                Log.d("VoiceTextTemplate", "匹配成功："+substring);
                voiceList.add(substring);
                if (!substring.equals(s)){
                    checkCharacterList(s.substring(i),voiceList, haveMoney);
                }
                return;
            }
        }
        //一个都没匹配到，则移除第一个字符，继续匹配剩下的
        if (!TextUtils.isEmpty(s) && s.length()>1){
            String substring = s.substring(1);
            Log.d("VoiceTextTemplate", "未匹配成功继续匹配："+substring);
            checkCharacterList(substring,voiceList, haveMoney);
        }
        Log.d("VoiceTextTemplate", "未匹配成功结束："+s);
    }

    private static List<String> getLetters(String input) {
        List<String> voiceList = new ArrayList<>();
        int len = input.length();
        for (int i = 0; i < len; i++) {
            char current = input.charAt(i);
            voiceList.add(current + "_");
        }
        return voiceList;
    }

    /**
     * 全转成 中文 RMB
     *
     * @param input
     * @return
     */
    private static List<String> genReadableMoney(String input) {
        List<String> voiceList = new ArrayList<>();
        if (input.contains(".")) {
            String integerPart = input.split("\\.")[0];
            String decimalPart = input.split("\\.")[1];
            List<String> intList = readIntPart(integerPart);
            List<String> decimalList = readDecimalPart(decimalPart);
            voiceList.addAll(intList);
            if (!decimalList.isEmpty()) {
                voiceList.add(DIAN);
                voiceList.addAll(decimalList);
            }
        } else {
            voiceList.addAll(readIntPart(input));
        }
        return voiceList;
    }

    private static List<String> readDecimalPart(String decimalPart) {
        List<String> result = new ArrayList<>();
        if (!"00".equals(decimalPart)) {
            char[] chars = decimalPart.toCharArray();
            for (char ch : chars) {
                result.add(ch + "_");
            }
        }
        return result;
    }

    /**
     * 全转成数字
     *
     * @param numString
     * @return
     */
    private static List<String> createReadableNumList(String numString) {
        List<String> result = new ArrayList<>();
        if (!TextUtils.isEmpty(numString)) {
            int len = numString.length();
            for (int i = 0; i < len; i++) {
                if ('.' == numString.charAt(i)) {
                    result.add(DIAN);
                } else {
                    result.add(numString.charAt(i) + "_");
                }
            }
        }
        return result;
    }

    /**
     * 返回数字对应的音频
     *
     * @param integerPart
     * @return
     */
    private static List<String> readIntPart(String integerPart) {
        List<String> result = new ArrayList<>();
        if (TextUtils.isEmpty(integerPart)){
            return result;
        }
        String intString = MoneyUtils.readInt(Integer.parseInt(integerPart));
        int len = intString.length();
        for (int i = 0; i < len; i++) {
            char current = intString.charAt(i);
            if (current == '拾') {
                result.add(UNIT_TEN);
            } else if (current == '佰') {
                result.add(UNIT_HUNDRED);
            } else if (current == '仟') {
                result.add(UNIT_THOUSAND);
            } else if (current == '万') {
                result.add(UNIT_TENTHOUSAND);
            } else if (current == '亿') {
                result.add(UNIT_HUNDREDMILLION);
            } else {
                result.add(current + "_");
            }
        }
        return result;
    }

    private static void initVoiceConsList() {

        if (voiceCharacterList == null){
            delayTimeList = new HashMap<>();
            voiceCharacterList = new HashMap<>();
            delayTimeList.put(UNIT_TEN, 440L);
            voiceCharacterList.put("十",UNIT_TEN);
            voiceCharacterList.put("拾",UNIT_TEN);
            delayTimeList.put(UNIT_HUNDRED, 400L);
            voiceCharacterList.put("百",UNIT_HUNDRED);
            voiceCharacterList.put("佰",UNIT_HUNDRED);
            delayTimeList.put(UNIT_THOUSAND, 390L);
            voiceCharacterList.put("千",UNIT_THOUSAND);
            voiceCharacterList.put("仟",UNIT_THOUSAND);
            delayTimeList.put(UNIT_TENTHOUSAND, 390L);
            voiceCharacterList.put("万",UNIT_TENTHOUSAND);
            delayTimeList.put(UNIT_HUNDREDMILLION, 390L);
            voiceCharacterList.put("亿",UNIT_HUNDREDMILLION);
            delayTimeList.put(CNG, 700L);
            voiceCharacterList.put("cng",CNG);
            voiceCharacterList.put("CNG",CNG);
            delayTimeList.put(LNG, 700L);
            voiceCharacterList.put("lng",LNG);
            voiceCharacterList.put("LNG",LNG);
            delayTimeList.put(CHAIYOU, 600L);
            voiceCharacterList.put("柴油",CHAIYOU);
            delayTimeList.put(DIAN, 240L);
            voiceCharacterList.put(".",DIAN);
            voiceCharacterList.put("点",DIAN);
            delayTimeList.put(GONGJIN, 500L);
            voiceCharacterList.put("公斤",GONGJIN);
            delayTimeList.put(HAO, 260L);
            voiceCharacterList.put("号",HAO);
            delayTimeList.put(HAOQIANG, 900L);
            voiceCharacterList.put("号枪",HAOQIANG);
            delayTimeList.put(JIFENDIKOU, 800L);
            voiceCharacterList.put("积分抵扣",JIFENDIKOU);
            delayTimeList.put(JIN, 350L);
            voiceCharacterList.put("斤",JIN);
            delayTimeList.put(LIFANG, 400L);
            voiceCharacterList.put("立方",LIFANG);
            delayTimeList.put(NIYOUYIBIXINDINGDAN, 1300L);
            voiceCharacterList.put("你有一笔新订单",NIYOUYIBIXINDINGDAN);
            delayTimeList.put(QIYOU, 500L);
            voiceCharacterList.put("汽油",QIYOU);
            delayTimeList.put(QINGQUCAN, 700L);
            voiceCharacterList.put("请取餐",QINGQUCAN);
            delayTimeList.put(QINGZHUNBEIJIUCAN, 1000L);
            voiceCharacterList.put("请准备就餐",QINGZHUNBEIJIUCAN);
            delayTimeList.put(SHENG, 260L);
            voiceCharacterList.put("升",SHENG);
            delayTimeList.put(SHOUKUAN, 470L);
            voiceCharacterList.put("收款",SHOUKUAN);
            delayTimeList.put(SHOUYIN, 460L);
            voiceCharacterList.put("收银",SHOUYIN);
            delayTimeList.put(WEIXIN, 460L);
            voiceCharacterList.put("微信",WEIXIN);
            delayTimeList.put(XIANJIN, 440L);
            voiceCharacterList.put("现金",XIANJIN);
            delayTimeList.put(YINHANGKA, 600L);
            voiceCharacterList.put("银行卡",YINHANGKA);
            delayTimeList.put(YOUHUI, 400L);
            voiceCharacterList.put("优惠",YOUHUI);
            delayTimeList.put(YUE, 500L);
            voiceCharacterList.put("余额",YUE);
            delayTimeList.put(YUAN, 400L);
            voiceCharacterList.put("元",YUAN);
            delayTimeList.put(ZHIFU, 500L);
            voiceCharacterList.put("支付",ZHIFU);
            delayTimeList.put(ZHIFUBAO, 550L);
            voiceCharacterList.put("支付宝",ZHIFUBAO);
            delayTimeList.put(YINGSHOU, 550L);
            voiceCharacterList.put("应收",YINGSHOU);
            delayTimeList.put(SHISHOU, 450L);
            voiceCharacterList.put("实收",SHISHOU);
            delayTimeList.put(CHONGZHI, 450L);
            voiceCharacterList.put("充值",CHONGZHI);
            delayTimeList.put(TIANRANQI, 550L);
            voiceCharacterList.put("天然气",TIANRANQI);
            delayTimeList.put(ZHANGHU, 450L);
            voiceCharacterList.put("账户",ZHANGHU);
            delayTimeList.put(NUMBER_ZERO, 450L);
            voiceCharacterList.put("0",NUMBER_ZERO);
            voiceCharacterList.put("零",NUMBER_ZERO);
            delayTimeList.put(NUMBER_ONE, 360L);
            voiceCharacterList.put("1",NUMBER_ONE);
            voiceCharacterList.put("一",NUMBER_ONE);
            voiceCharacterList.put("壹",NUMBER_ONE);
            delayTimeList.put(NUMBER_TWO, 360L);
            voiceCharacterList.put("2",NUMBER_TWO);
            voiceCharacterList.put("二",NUMBER_TWO);
            voiceCharacterList.put("贰",NUMBER_TWO);
            delayTimeList.put(NUMBER_THREE, 400L);
            voiceCharacterList.put("3",NUMBER_THREE);
            voiceCharacterList.put("三",NUMBER_THREE);
            voiceCharacterList.put("叁",NUMBER_THREE);
            delayTimeList.put(NUMBER_FOUR, 430L);
            voiceCharacterList.put("4",NUMBER_FOUR);
            voiceCharacterList.put("四",NUMBER_FOUR);
            voiceCharacterList.put("肆",NUMBER_FOUR);
            delayTimeList.put(NUMBER_FIVE, 400L);
            voiceCharacterList.put("5",NUMBER_FIVE);
            voiceCharacterList.put("五",NUMBER_FIVE);
            voiceCharacterList.put("伍",NUMBER_FIVE);
            delayTimeList.put(NUMBER_SIX, 400L);
            voiceCharacterList.put("6",NUMBER_SIX);
            voiceCharacterList.put("六",NUMBER_SIX);
            voiceCharacterList.put("陆",NUMBER_SIX);
            delayTimeList.put(NUMBER_SEVEN, 400L);
            voiceCharacterList.put("7",NUMBER_SEVEN);
            voiceCharacterList.put("七",NUMBER_SEVEN);
            voiceCharacterList.put("柒",NUMBER_SEVEN);
            delayTimeList.put(NUMBER_EIGHT, 400L);
            voiceCharacterList.put("8",NUMBER_EIGHT);
            voiceCharacterList.put("八",NUMBER_EIGHT);
            voiceCharacterList.put("捌",NUMBER_EIGHT);
            delayTimeList.put(NUMBER_NINE, 400L);
            voiceCharacterList.put("9",NUMBER_NINE);
            voiceCharacterList.put("九",NUMBER_NINE);
            voiceCharacterList.put("玖",NUMBER_NINE);
            delayTimeList.put(A, 260L);
            voiceCharacterList.put("a",A);
            voiceCharacterList.put("A",A);
            delayTimeList.put(B, 260L);
            voiceCharacterList.put("b",B);
            voiceCharacterList.put("B",B);
            delayTimeList.put(C, 260L);
            voiceCharacterList.put("c",C);
            voiceCharacterList.put("C",C);
            delayTimeList.put(D, 260L);
            voiceCharacterList.put("d",D);
            voiceCharacterList.put("D",D);
            delayTimeList.put(E, 260L);
            voiceCharacterList.put("E",E);
            voiceCharacterList.put("e",E);
            delayTimeList.put(F, 300L);
            voiceCharacterList.put("f",F);
            voiceCharacterList.put("F",F);
            delayTimeList.put(G, 260L);
            voiceCharacterList.put("g",G);
            voiceCharacterList.put("G",G);
            delayTimeList.put(H, 360L);
            voiceCharacterList.put("h",H);
            voiceCharacterList.put("H",H);
            delayTimeList.put(I, 260L);
            voiceCharacterList.put("i",I);
            voiceCharacterList.put("I",I);
            delayTimeList.put(J, 260L);
            voiceCharacterList.put("j",J);
            voiceCharacterList.put("J",J);
            delayTimeList.put(K, 260L);
            voiceCharacterList.put("k",K);
            voiceCharacterList.put("K",K);
            delayTimeList.put(L, 300L);
            voiceCharacterList.put("l",L);
            voiceCharacterList.put("L",L);
            delayTimeList.put(M, 300L);
            voiceCharacterList.put("m",M);
            voiceCharacterList.put("M",M);
            delayTimeList.put(N, 260L);
            voiceCharacterList.put("n",N);
            voiceCharacterList.put("N",N);
            delayTimeList.put(O, 260L);
            voiceCharacterList.put("o",O);
            voiceCharacterList.put("O",O);
            delayTimeList.put(P, 260L);
            voiceCharacterList.put("p",P);
            voiceCharacterList.put("P",P);
            delayTimeList.put(Q, 260L);
            voiceCharacterList.put("q",Q);
            voiceCharacterList.put("Q",Q);
            delayTimeList.put(R, 260L);
            voiceCharacterList.put("r",R);
            voiceCharacterList.put("R",R);
            delayTimeList.put(S, 260L);
            voiceCharacterList.put("s",S);
            voiceCharacterList.put("S",S);
            delayTimeList.put(T, 260L);
            voiceCharacterList.put("t",T);
            voiceCharacterList.put("T",T);
            delayTimeList.put(U, 260L);
            voiceCharacterList.put("u",U);
            voiceCharacterList.put("U",U);
            delayTimeList.put(V, 260L);
            voiceCharacterList.put("v",V);
            voiceCharacterList.put("V",V);
            delayTimeList.put(W, 280L);
            voiceCharacterList.put("w",W);
            voiceCharacterList.put("W",W);
            delayTimeList.put(X, 280L);
            voiceCharacterList.put("x",X);
            voiceCharacterList.put("X",X);
            delayTimeList.put(Y, 260L);
            voiceCharacterList.put("y",Y);
            voiceCharacterList.put("Y",Y);
            delayTimeList.put(Z, 260L);
            voiceCharacterList.put("z",Z);
            voiceCharacterList.put("Z",Z);
        }
    }
}
