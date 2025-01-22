package com.company.admin.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Created by JQ棣 on 11/2/17.
 */
public class ImageUploaderTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	/**
     * uploader id 唯一
     */
    private String identifier;

    /**
     * input 标签的name值
     */
    private String name;

    /**
     * 上传的文件夹名称
     */
    private String folder;

    private Integer width = 80;

    private Integer height = 80;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        JspWriter out = pageContext.getOut();
        String template = "\n" +
                "<div id=\"previewDiv_{{id}}\" class=\"img-preview\"></div>\n" +
                "<input id=\"input_{{id}}\" class=\"hidden\" type=\"file\"\n" +
                "       onchange=\"uploadPreview_{{id}}(this)\"\n" +
                "       accept=\"image/png,image/jpg,image/jpeg,image/bmp,image/gif\"/>\n" +
                "<input id=\"url_{{id}}\" type=\"hidden\" name=\"{{name}}\"/>\n" +
                "\n" +
                "<script>\n" +
                "    var images_{{id}} = [];\n" +
                "    var $previewDiv_{{id}} = $(\"#previewDiv_{{id}}\");\n" +
                "    var $input_{{id}} = $(\"#input_{{id}}\");\n" +
                "    var $url_{{id}} = $(\"#url_{{id}}\");\n" +
                "    var lock_{{id}} = false;\n" +
                "\n" +
                "    function updatePreviewDiv_{{id}}() {\n" +
                "        var html = \"\";\n" +
                "        $.each(images_{{id}}, function (index, image) {\n" +
                "            html += \"<label style=\'width:" + width + "px;height:" + height + "px\'>\" +\n" +
                "                \"<img src='\" + image + \"'/>\" +\n" +
                "                \"<a class='delete' href='#' onclick='deleteUpload_{{id}}(\" + index + \");return false;'>删除\" +\n" +
                "                \"</a>\" +\n" +
                "                \"</label>\";\n" +
                "        });\n" +
                "        if (images_{{id}}.length === 0) {\n" +
                "            html += \"<label style=\'width:" + width + "px;height:" + height + "px\'>\" +\n" +
                "                \"<a id='addImage_{{id}}' class='add' href='#' onclick='selectImage_{{id}}();return false;'>\" +\n" +
                "                \"<i class='fa fa-plus'  style='line-height:" + height + "px'></i>\" +\n" +
                "                \"</a>\" +\n" +
                "                \"</label>\";\n" +
                "        }\n" +
                "        $previewDiv_{{id}}.html(html);\n" +
                "    }\n" +
                "    function selectImage_{{id}}() {\n" +
                "        if (!lock_{{id}})\n" +
                "            $input_{{id}}.click();\n" +
                "    }\n" +
                "    $(function () {\n" +
                "        updatePreviewDiv_{{id}}();\n" +
                "    });\n" +
                "\n" +
                "    function uploadPreview_{{id}}(file) {\n" +
                "        if (file.files && file.files[0]) {\n" +
                "            lock_{{id}} = true;\n" +
                "            $(\"#addImage_{{id}}\").html(\"<i class='fa fa-spinner fa-pulse'  style='line-height:"+height+"px'></i>\");\n" +
                "\n" +
                "            imageUpload(\"<%=request.getContextPath()%>/admin/common/api/file/upload\", '{{folder}}', file.files[0], function (data) {\n" +
                "                lock_{{id}} = false;\n" +
                "                if (data.status) {\n" +
                "                    var url = data.data[0];\n" +
                "                    images_{{id}}.push(url);\n" +
                "                    $url_{{id}}.val(url);\n" +
                "                } else {\n" +
                "                    alert(data.message);\n" +
                "                }\n" +
                "                updatePreviewDiv_{{id}}();\n" +
                "            }, function (res) {\n" +
                "                alert(\"请求失败：\" + res.statusText + \"\\n错误码：\" + res.status);\n" +
                "                lock_{{id}} = false;\n" +
                "                updatePreviewDiv_{{id}}();\n" +
                "            });\n" +
                "            $input_{{id}}.val(\"\");\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    function deleteUpload_{{id}}(index) {\n" +
                "        images_{{id}}.splice(index, 1);\n" +
                "        $url_{{id}}.val(\"\");\n;" +
                "        updatePreviewDiv_{{id}}();\n" +
                "    }\n" +
                "</script>";
        template = template.replaceAll("\\{\\{id}}", identifier)
                .replaceAll("\\{\\{name}}", name)
                .replaceAll("\\{\\{folder}}", folder)
                .replaceAll("<%=request.getContextPath\\(\\)%>", request.getContextPath());
        try {
            out.println(template);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.doStartTag();
    }

}
