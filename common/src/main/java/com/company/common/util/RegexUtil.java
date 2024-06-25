package com.company.common.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class RegexUtil {
	// 邮箱
	private static final String PATTERN_EMAIL = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	// 18位身份证正则
	private static final String PATTERN_IDNUMBER_18_BITS = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
	// 手机号码
	private static final String PATTERN_MOBILE = "^(13[0-9]|14[0-1,4-9]|15[0-9]|16[5-6]|17[0-8]|18[0-9]|19[89])\\d{8}$";
	
	// HTML标签正则表达式
	private static final String PATTERN_HTML = "<[^>]+>";
	private static final String PATTERN_HTML_SPECIAL = "\\&[a-zA-Z]{1,10};";
	
	/**
	 * 正则验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		Pattern p = Pattern.compile(PATTERN_EMAIL);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	/**
	 * 正则验证身份证号
	 * 
	 * @param idNumber
	 * @return
	 */
	public static boolean checkIdNumber(String idNumber) {
		if (StringUtils.isBlank(idNumber)) {
			return false;
		}
		Pattern p = Pattern.compile(PATTERN_IDNUMBER_18_BITS);
		Matcher m = p.matcher(idNumber);
		return m.matches();
	}

	/**
	 * 根据身份证号获取性别，1-男，2-女
	 * 
	 * @param idNumber
	 * @return
	 */
	public static Integer getSexByIdNumber(String idNumber) {
		if (!checkIdNumber(idNumber)) {
			return null;
		}
		String sex = null;
		if (idNumber.length() == 18) {
			sex = idNumber.substring(16, 17);
		} else if (idNumber.length() == 15) {
			sex = idNumber.substring(14, 15);
		}
		return Integer.parseInt(sex) % 2 == 0 ? 2 : 1;
	}

	/**
	 * 根据身份证号获取出生年份
	 * 
	 * @param idNumber
	 * @return
	 */
	public static Integer getBirthYearByIdNumber(String idNumber) {
		if (!checkIdNumber(idNumber)) {
			return null;
		}
		return Integer.valueOf(idNumber.substring(6, 10));
	}
	
	/**
	 * 根据身份证号获取年龄
	 * 
	 * @param idNumber
	 * @return
	 */
	public static Integer getAgeByIdNumber(String idNumber) {
		if (!checkIdNumber(idNumber)) {
			return null;
		}
		
		int year = Integer.parseInt(idNumber.substring(6,10));
		int month = Integer.parseInt(idNumber.substring(10,12));
		int day = Integer.parseInt(idNumber.substring(12,14));
    	
		LocalDate birthDate = LocalDate.of(year, month, day);
		
		long years = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
		
		return Long.valueOf(years).intValue();
	}
	
	/**
	 * 根据身份证号获取生日
	 * 
	 * @param idNumber
	 * @return
	 */
	public static Date getBirthdayByIdNumber(String idNumber) {
		if (!checkIdNumber(idNumber)) {
			return null;
		}
		
		int year = Integer.parseInt(idNumber.substring(6,10));
		int month = Integer.parseInt(idNumber.substring(10,12));
		int day = Integer.parseInt(idNumber.substring(12,14));
		
		Calendar instance = Calendar.getInstance();
		instance.set(year, month - 1, day, 0, 0, 0);
		return instance.getTime();
	}
	
	/**
	 * 正则验证手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean checkMobile(String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return false;
		}
		Pattern p = Pattern.compile(PATTERN_MOBILE);
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 去除html标签
	 * 
	 * @param htmlContent
	 * @return
	 */
	public static String removeHtmlTag(String htmlContent) {
		if (StringUtils.isBlank(htmlContent)) {
			return "";
		}
		Pattern htmlPattern = Pattern.compile(PATTERN_HTML, Pattern.CASE_INSENSITIVE);
		Matcher htmlMatcher = htmlPattern.matcher(htmlContent);
		htmlContent = htmlMatcher.replaceAll(""); // 过滤html标签

		Pattern specialPattern = Pattern.compile(PATTERN_HTML_SPECIAL, Pattern.CASE_INSENSITIVE);
		Matcher specialMatcher = specialPattern.matcher(htmlContent);
		htmlContent = specialMatcher.replaceAll(""); // 过滤特殊标签

		return htmlContent;
	}

	public static boolean match(String pattern, String str) {
		pattern = pattern.replace('.', '#');
		pattern = pattern.replaceAll("#", "\\\\.");
		pattern = pattern.replace('*', '#');
		pattern = pattern.replaceAll("#", ".*");
		pattern = pattern.replace('?', '#');
		pattern = pattern.replaceAll("#", ".?");
		pattern = "^" + pattern + "$";

		Pattern p = Pattern.compile(pattern);
		Matcher fMatcher = p.matcher(str);
		if (fMatcher.matches()) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
//		String idNumber = "440983199305185872";
//		System.out.println(RegexUtil.checkIdNumber(idNumber));
//		System.out.println(RegexUtil.getSexByIdNumber(idNumber));
//		System.out.println(RegexUtil.getBirthYearByIdNumber(idNumber));
//		System.out.println(RegexUtil.getAgeByIdNumber(idNumber));
//		System.out.println(RegexUtil.getBirthdayByIdNumber(idNumber));
		
//		String mobile = "16722222212";
//		System.out.println(RegexUtil.checkMobile(mobile));
		Pattern p = Pattern.compile("^([0-9]{4})[\\-](0?[1-9]|1[0-2])$");
		Matcher m = p.matcher("18-1");
		System.out.println(m.matches());
		////
		//String content = "<section>独角兽们HTML中&gt;的含义 -仍在“挨饿”，投资者还在排队“送钱”</section><section><br/></section><p><br/></p><section><p style=\"margin: 0px 0px 26px; text-align: left; -ms-word-wrap: break-word; box-sizing: border-box;\"><span style=\"box-sizing: border-box;\">投资者们正不断给独角兽投入更多的资金。</span></p><p style=\"margin: 0px 0px 26px; text-align: left; -ms-word-wrap: break-word; box-sizing: border-box;\">Crunchbase的分析数据显示，今年全球独角兽融资有望超过2017年创下的纪录：在2018年的过去7个月里，投资者对估值10亿美元以上的公司投入了730亿美元，大约是2017年全年总额的四分之三。</p><p style=\"margin: 0px 0px 26px; text-align: left; -ms-word-wrap: break-word; box-sizing: border-box;\">此外，独角兽的区域分布、所属行业、数量等方面都在发生相应变化。</p><p style=\"margin: 0px 0px 26px; text-align: left; -ms-word-wrap: break-word; box-sizing: border-box;\">&nbsp;</p><p style=\"margin: 0px 0px 26px; text-align: left; -ms-word-wrap: break-word; box-sizing: border-box;\"><strong><span style=\"color: rgb(66, 133, 244); font-family: Helvetica;\">|&nbsp;</span></strong><strong><span style=\"color: rgb(66, 133, 244); font-family: Helvetica;\">数量与区域分布</span></strong></p><p style=\"margin: 0px 0px 26px; text-align: left; -ms-word-wrap: break-word; box-sizing: border-box;\"><strong><span style=\"color: rgb(66, 133, 244); font-family: Helvetica;\"><br/></span></strong></p><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\">数据显示，2018年以来，新晋独角兽数量“一路狂奔”：截至目前已有65家“新来者”，而2017年全年只有82家。</p><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\">从区域上看，2018年的地域划分与过去几年的大格局并无太大不同：美国和中国仍占绝大多数，新晋独角兽数量相当<span style=\"box-sizing: border-box;\">；</span><span style=\"box-sizing: border-box;\">印度、英国和欧洲占大多数。</span></p><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\"><span style=\"box-sizing: border-box;\"><br/></span></p><p style=\"text-align: center;\"><img width=\"375\" height=\"344\" title=\"f506djrigrjswp2k.jpg\" style=\"width: 375px; height: 344px;\" alt=\"f506djrigrjswp2k.jpg\" src=\"https://shuttle-hailaohui.oss-cn-shenzhen.aliyuncs.com/ueditor/image/20180811/1533994291505087073.jpg\"/></p><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\"><span style=\"box-sizing: border-box;\"><br/></span><strong><span style=\"color: rgb(66, 133, 244); font-family: Helvetica;\"><br/></span></strong></p><address style=\"margin: 1.6rem 0px; border: 0px currentColor; border-image: none; color: rgb(66, 133, 244); text-transform: none; line-height: 29px; text-indent: 0px; letter-spacing: normal; padding-left: 15px; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; position: relative; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\"><p style=\"margin: 0px 0px 26px; -ms-word-wrap: break-word; box-sizing: border-box;\"><strong style=\"font-weight: 600; box-sizing: border-box;\">|&nbsp;新晋独角兽引人注目</strong></p></address><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\">今年新增的65家独角兽集中在媒体、无人驾驶、共享出行、咖啡、密码、支付、制药、房地产、物流、旅游等领域，也几乎代表了所有重要的创业领域。另外还有几个引人关注的现象：</p><ul class=\" list-paddingleft-2\" style=\"list-style-type: none;\"><li><p style=\"margin: 0px 0px 26px; -ms-word-wrap: break-word; box-sizing: border-box;\">首先，拼车和最后一英里出行领域仍在出现新的独角兽。其中比较典型的有美国滑板车共享公司Bird和Lime，爱沙尼亚的打车平台Taxify和西班牙的Cabify。</p></li><li><p style=\"margin: 0px 0px 26px; -ms-word-wrap: break-word; box-sizing: border-box;\">其次，个性化推荐平台引起热议。包括2018年新独角兽：个性化电商品牌About You，音乐流媒体服务商平台Deezer、新闻推荐App趣头条。</p></li><li><p style=\"margin: 0px 0px 26px; -ms-word-wrap: break-word; box-sizing: border-box;\">最后，区块链企业也突破“数十亿美元估值”大关、出现独角兽，其中包括加密货币挖掘所用芯片的开发商Bitmain和加密交易平台Circle。</p></li></ul><p style=\"margin: 0px 0px 26px; -ms-word-wrap: break-word; box-sizing: border-box;\">&nbsp;</p><address style=\"margin: 1.6rem 0px; border: 0px currentColor; border-image: none; color: rgb(66, 133, 244); text-transform: none; line-height: 29px; text-indent: 0px; letter-spacing: normal; padding-left: 15px; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; position: relative; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\"><p style=\"margin: 0px 0px 26px; -ms-word-wrap: break-word; box-sizing: border-box;\"><strong style=\"font-weight: 600; box-sizing: border-box;\">|&nbsp;老牌独角兽仍在吸金</strong></p></address><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\">当然，不仅仅是新独角兽，投资者们也在向老牌独角兽“疯狂砸钱”。</p><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\">截至2018年，已有新独角兽公司和现有独角兽公司披露170轮股票融资，总计730亿美元。相比之下，2017年全年有260轮，总价值982亿美元。</p><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\">2018年的融资总额中，近五分之一流向了蚂蚁金服，6月完成140亿美元融资。这几乎是有史以来规模最大的C轮融资。<span style=\"box-sizing: border-box;\">其它获得数十亿美元融资的公司还有新加坡网约车平台Grab和京东物流：前者在今年8月初宣布获得20亿美元融资，后者于2月宣布获得25亿美元融资。</span></p><p style=\"margin: 0px 0px 26px; color: rgb(61, 70, 77); text-transform: none; text-indent: 0px; letter-spacing: normal; font-family: &quot;PingFang SC&quot;, &quot;Lantinghei SC&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Microsoft YaHei&quot;, &quot;微软雅黑&quot;, STHeitiSC-Light, simsun, &quot;宋体&quot;, &quot;WenQuanYi Zen Hei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 16px; font-style: normal; font-weight: normal; word-spacing: 0px; white-space: normal; -ms-word-wrap: break-word; box-sizing: border-box; orphans: 2; widows: 2; background-color: rgb(255, 255, 255); font-variant-ligatures: normal; font-variant-caps: normal; -webkit-text-stroke-width: 0px;\"><strong style=\"font-weight: 600; box-sizing: border-box;\"><span style=\"box-sizing: border-box;\">整体来说，目前独角兽们普遍仍在“挨饿”，大量投资者也在排队“补给”。</span></strong><strong><span style=\"color: rgb(66, 133, 244); font-family: Helvetica;\"><br/></span></strong></p></section><p><br/></p><p style=\"text-align: center;\">&nbsp;</p>";
		//System.out.println(RegexUtil.removeHtmlTag(content));
	}
}
