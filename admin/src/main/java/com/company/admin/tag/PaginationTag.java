package com.company.admin.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.company.admin.entity.base.XSPageModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 分页标签
 * Created by JQ棣 on 11/2/17.
 */
public class PaginationTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	private static final String PAGE = "page";           //分页输出参数

    private static final String LAST = "最后一页";
    private static final String FIRST = "第一页";
    private static final String NEXT = "下一页";
    private static final String BEFORE = "上一页";

    private static final Long SHOW_PER_PAGE = 7L;              //最大可选页码
    private static final Long PAGE_SIDE = 2L;                  //最后两名可选页码


    private XSPageModel<?> pageModel;

    private Long limit;

    public void setPageModel(XSPageModel<?> pageModel) {
        this.pageModel = pageModel;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }


    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Long page = request.getParameter(PAGE) == null ? 1L : Long.parseLong(request.getParameter(PAGE));
        Long total = pageModel.getTotal() == null ? 0L : pageModel.getTotal();
        if (limit == null || limit <= 0) {
            limit = 10L;
        }
        Long maxPage = (long) Math.ceil(total * 1.0 / limit);

        //确定分页
        if (maxPage > 0) {
            JspWriter out = pageContext.getOut();
            String queryString = "";
            try {
                queryString = params2String(request);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String first;
            if (page <= 1) {
                first = "<li class=\"disabled xs-icon\"><a title=\"" + FIRST + "\"><i class=\"xs-pagination-icon xs-pagination-double-left\"></i></a></li>" +
                        "<li class=\"disabled xs-icon\"><a title=\"" + BEFORE + "\"><i class=\"xs-pagination-icon xs-pagination-left\"></i></a></li>";
            } else {
                first = "<li class=\"xs-icon\"><a href=\"?" + PAGE + "=1" + queryString + "\"><i class=\"xs-pagination-icon xs-pagination-double-left\" title=\"" + FIRST + "\"></i></a></li>" +
                        "<li class=\"xs-icon\"><a href=\"?" + PAGE + "=" + String.valueOf(page - 1) + queryString + "\"  title=\"" + BEFORE + "\"><i class=\"xs-pagination-icon xs-pagination-left\"></i></a></li>";
            }

            String last;
            if (page >= maxPage) {
                last = "<li class=\"disabled xs-icon\"><a title=\"" + NEXT + "\"><i class=\"xs-pagination-icon xs-pagination-right\"></i></a></li>" +
                        "<li class=\"disabled xs-icon\"><a title=\"" + LAST + "\"><i class=\"xs-pagination-icon xs-pagination-double-right\"></i></a></li>";
            } else {
                last = "<li class=\"xs-icon\"><a href=\"?" + PAGE + "=" + String.valueOf(page + 1) + queryString + "\" title=\"" + NEXT + "\"><i class=\"xs-pagination-icon xs-pagination-right\"></i></a></li>" +
                        "<li class=\"xs-icon\"><a href=\"?" + PAGE + "=" + maxPage + queryString + "\" title=\"" + LAST + "\"><i class=\"xs-pagination-icon xs-pagination-double-right\"></i></a></li>";
            }

            StringBuilder center = new StringBuilder();
            if (maxPage <= SHOW_PER_PAGE) {
                for (long i = 1; i <= maxPage; i++) {
                    if (i == page)
                        center.append("<li class=\"active\" ><a>").append(String.valueOf(i)).append("</a></li>");
                    else
                        center.append("<li><a href=\"").append("?").append(PAGE).append("=").append(String.valueOf(i)).append(queryString).append("\">").append(String.valueOf(i)).append("</a></li>");
                }
            } else {
                if (page >= SHOW_PER_PAGE && (maxPage - page) >= SHOW_PER_PAGE) {
                    center = new StringBuilder("<li><a>...</a></li>");
                    for (long i = page - PAGE_SIDE; i <= page + PAGE_SIDE; i++) {
                        if (i == page)
                            center.append("<li class=\"active\" ><a title=\"第").append(i).append("页\">").append(String.valueOf(i)).append("</a></li>");
                        else
                            center.append("<li><a href=\"").append("?").append(PAGE).append("=").append(String.valueOf(i)).append(queryString).append("\" title=\"第").append(i).append("页\">").append(String.valueOf(i)).append("</a></li>");
                    }
                    center.append("<li><a>...</a></li>");
                } else if (page >= SHOW_PER_PAGE) {
                    center = new StringBuilder("<li><a>...</a></li>");
                    for (long i = maxPage - SHOW_PER_PAGE; i <= maxPage; i++) {
                        if (i == page)
                            center.append("<li class=\"active\" ><a title=\"第").append(i).append("页\">").append(String.valueOf(i)).append("</a></li>");
                        else
                            center.append("<li><a href=\"").append("?").append(PAGE).append("=").append(String.valueOf(i)).append(queryString).append("\" title=\"第").append(i).append("页\">").append(String.valueOf(i)).append("</a></li>");
                    }
                } else {
                    for (int i = 1; i <= SHOW_PER_PAGE; i++) {
                        if (i == page)
                            center.append("<li class=\"active\" ><a title=\"第").append(i).append("页\">").append(String.valueOf(i)).append("</a></li>");
                        else
                            center.append("<li><a href=\"").append("?").append(PAGE).append("=").append(String.valueOf(i)).append(queryString).append("\" title=\"第").append(i).append("页\">").append(String.valueOf(i)).append("</a></li>");
                    }
                    center.append("<li><a>...</a></li>");
                }
            }
            StringBuilder paginationStr = new StringBuilder();

            paginationStr.append("<div class=\"xs-pagination\">")
                    .append("<div class=\"pagination-record\">共").append(total).append("条记录</div>")
                    .append("<ul>").append(first).append(center).append(last).append("</ul>")
                    .append("<div class=\"pagination-extra\">")
                    .append("<span class=\"pagination-total-count\">").append(page).append("/").append(maxPage).append("</span>")
                    .append("<span class=\"pagination-change\"></span>")
                    .append("<input id=\"xsPage\" type=\"number\" min=\"1\" step=\"1\" max=\"" + maxPage + "\">")
                    .append("<span class=\"pagination-change-page\"></span>")
                    .append("<a href=\"#\" class=\"submit\" onclick=\"xsChangePageSubmit();return false;\">确定</a>")
                    .append("</div>")
                    .append("</div>")
                    .append("<script>")
                    .append("var $xsPage=$(\"#xsPage\");\n")
                    .append("function xsChangePageSubmit() {\n")
                    .append("var p=$xsPage.val();\n")
                    .append("if(p!==\"\"&&p!==undefined&&p!==null){\n")
                    .append("window.location.href=\"?page=\"+p+\"").append(queryString).append("\";\n")
                    .append("}\n")
                    .append("return false;\n")
                    .append("}\n")
                    .append("</script>");
            try {
                out.print(paginationStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.doStartTag();
    }

    private String params2String(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String[]> params = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (entry.getKey().equals(PAGE))
                continue;
            if (entry.getKey().equals("limit"))
                continue;
            sb.append("&").append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(entry.getValue()[0], "UTF-8"));
        }
        sb.append("&limit=").append(limit);
        return sb.toString();
    }
}
