function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

function delCookie(name)
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}


//这是有设定过期时间的使用示例：
//s20是代表20秒
//h是指小时，如12小时则是：h12
//d是天数，30天则：d30
//w是周，1周则：w1;
//m是月数，设置1个月就是30天：1m，2个月就是60天：2m，如此类推;
function setCookie(name,value,time,path)
{
    var second = getSecond(time);
    var exp = new Date();
    exp.setTime(exp.getTime() + second*1000);
    if (isNotBlank(path)) {
        path = ";path=".concat(path);
    } else {
        path = "";
    }
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + path;
}

function getSecond(str) {
    var str1 = str.substring(1, str.length) * 1;
    var str2 = str.substring(0, 1);
    if (str2 == "s") {
        return str1;
    }
    else if (str2 == "h") {
        return str1 * 3600;
    }
    else if (str2 == "d") {
        return str1 * 24 * 3600;
    }
    else if (str2 == "w") {
        return str1 * 7 * 24 * 3600;
    }
    else if (str2 == "m") {
        return str1 * 30 * 24 * 3600;
    }
}