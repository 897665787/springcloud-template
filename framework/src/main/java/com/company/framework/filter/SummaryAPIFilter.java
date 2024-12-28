package com.company.framework.filter;

import com.company.common.constant.CommonConstants;
import com.company.common.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 打印API耗时
 */
@Slf4j(topic = "LOG_SUMMARY_API")
@Component
@Order(CommonConstants.FilterOrdered.SUMMARY_API)
public class SummaryAPIFilter extends OncePerRequestFilter {

	@Value("${template.requestFilter.ignoreLogPatterns:}")
	private String ignoreLogPatterns;
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if (StringUtils.isBlank(ignoreLogPatterns)) {
			return false;
		}
		
		String requestURI = request.getRequestURI();
		String[] ignoreLogPatternss = StringUtils.split(ignoreLogPatterns, ",");
		for (String ignoreLogPattern : ignoreLogPatternss) {
			boolean match = RegexUtil.match(ignoreLogPattern, requestURI);
			if (match) {
				// 匹配任意一个表达式就不打印日志
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long start = System.currentTimeMillis();
		String method = request.getMethod();
		String requestURI = request.getRequestURI();
		chain.doFilter(request, response);

		log.info("{}|{}|{}", method, transformUrl(requestURI), System.currentTimeMillis() - start);
	}

	/**
	 * 将URL中的数字替换为?，解决@PathVariable参数导致的接口路径不一致问题
	 *
	 * <pre>
	 * /get-params-none/{id} -> /get-params-none/?
	 * /{id}/get-params-none -> /?/get-params-none
	 * /get-params-none-v2/{id} -> /get-params-none-v2/?
	 * </pre>
	 *
	 * @param url
	 * @return
	 */
	public static String transformUrl(String url) {
		// 定义正则表达式，匹配/后或者//中间的纯数字
		String regex = "(?<=/)(\\d+)(?=/|$)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);

		// 使用?替换匹配到的纯数字
		return matcher.replaceAll("?");
	}

	public static void main(String[] args) {
		String[] testUrls = {
				"/reqlog/get-params-none/3",
				"/reqlog/get-params-none/344",
				"/reqlog/get-params-none-v2/3",
				"/reqlog/3/get-params-none-v2"
		};

		for (String url : testUrls) {
			System.out.println("输入：" + url + " 输出：" + transformUrl(url));
		}
	}
}