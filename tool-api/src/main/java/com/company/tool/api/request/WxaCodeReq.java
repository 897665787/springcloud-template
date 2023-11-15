package com.company.tool.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 接口B: 获取小程序码（永久有效、数量暂无限制）.
 * 
 * <pre>
 * 通过该接口生成的小程序码，永久有效，数量暂无限制。
 * 用户扫描该码进入小程序后，将统一打开首页，开发者需在对应页面根据获取的码中 scene 字段的值，再做处理逻辑。
 * 使用如下代码可以获取到二维码中的 scene 字段的值。
 * 调试阶段可以使用开发工具的条件编译自定义参数 scene=xxxx 进行模拟，开发工具模拟时的 scene 的参数值需要进行 urlencode
 * </pre>
 *
 * @param scene
 *            最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+;/:;=?@-._~，
 *            其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
 * @param page
 *            必须是已经发布的小程序页面，例如 "pages/index/index" ;如果不填写这个字段，默认跳主页面
 * @param checkPath
 *            默认true 检查 page 是否存在，为 true 时 page 必须是已经发布的小程序存在的页面（否则报错）； 为 false
 *            时允许小程序未发布或者 page 不存在，但 page 有数量上限（60000个）请勿滥用
 * @param width
 *            默认430 二维码的宽度
 * @param autoColor
 *            默认true 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
 * @param lineColor
 *            autoColor 为 false 时生效，使用 rgb 设置颜色 例如
 *            {"r":"xxx";"g":"xxx";"b":"xxx"}
 * @param isHyaline
 *            是否需要透明底色， is_hyaline 为true时，生成透明底色的小程序码
 * @return 文件内容字节数组
 * @throws WxErrorException
 *             异常
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WxaCodeReq {
	/**
	 * 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+;/:;=?@-._~，
	 * 其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
	 */
	@NotBlank(message = "scene不能为空")
	@Size(max = 32, message = "[scene]允许的最大长度为32")
	String scene;

	/* 以上为必填参数 */

	/* 以下为非必填参数 */

	/**
	 * 必须是已经发布的小程序页面，例如 "pages/index/index" ;如果不填写这个字段，默认跳主页面
	 */
	String page;

	/**
	 * 默认true 检查 page 是否存在，为 true 时 page 必须是已经发布的小程序存在的页面（否则报错）； 为 false
	 * 时允许小程序未发布或者 page 不存在，但 page 有数量上限（60000个）请勿滥用
	 */
	Boolean checkPath;

	/**
	 * 默认430，二维码的宽度，单位 px，最小 280px，最大 1280px
	 */
	Integer width;

	/**
	 * 默认true 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
	 */
	Boolean autoColor;

	/**
	 * autoColor 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx";"g":"xxx";"b":"xxx"}
	 */
	LineColor lineColor;

	/**
	 * 是否需要透明底色， is_hyaline 为true时，生成透明底色的小程序码
	 */
	Boolean isHyaline;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	@AllArgsConstructor
	public static class LineColor {
		String r;
		String g;
		String b;
	}
}
