<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <title></title>
    <style type="text/css">
        body {
            font-family: pingfang sc light;
            font-size: 120%;
        }
        .right{
            text-align: right;
            width: 90%;
        }
        .hTitle {
            text-align: center;
            width: 100%;
            font-size: 180%;
        }
        .text{
            text-indent: 2em;
            line-height: 30px;
        }
        .underline{
            text-decoration:underline;
        }
        .
    </style>
</head>


<body>

<div class="page" >
    <div class="hTitle"><p><strong>${org}</strong></p></div>
    <div class="hTitle"><p><strong>建设工程竣工验收消防备案凭证（未抽中）</strong></p></div>
    <div class="right"><p>东住建消竣备字(${word})第${fireCheck.id?string["0"]}号</p></div>
    <div><p><strong class="underline">${joinCorp.name!}</strong>：</p></div>
    <div class="text"><p>根据《中华人民共和国消防法》第十三条及相关规定，你单位<strong class="underline">${fireCheck.applyTime?string("yyyy年MM月dd日")}</strong>申请的建设工程（地址：<strong class="underline">${fireCheck.info.project.address!}</strong> ）竣工验收消防备案，提供了下列材料：</p></div>

    <#if fireCheck.files?exists>
        <#list fireCheck.files?sort_by("id")  as  file>
            <#if file.pass == true>
                <p style="line-height: 30px">√ ${file_index + 1}.${file.name}</p>
            <#else>
                <p style="line-height: 30px">× ${file_index + 1}.${file.name}</p>
            </#if>
        </#list>
    </#if>
    <p class="text"> 经审查，备案材料齐全，依法核发备案凭证。已经依法进行竣工验收消防备案的建设工程，如需扩建、改建(含室内外装修、建筑保温、用途变更)的，应当依法向消防设计审查机构申请施工图设计文件审查或备案；属于公众聚集场所的，投入使用、营业前应向消防部门作出符合消防安全标准的承诺。</p>
    <p class="text">（注：备案编号${fireCheck.id?string["0"]},该项目未抽中。）</p>

    <#--    <#list testArray as test>-->
<#--        ${test}-->
<#--    </#list>-->

    <table border="0" width="100%" >
        <tr>
            <td></td>
            <td class="right" style="font-size: 110%" height="30px">
                <p>东港市住房和城乡建设局</p>
            </td>
        </tr>
        <tr>
            <td></td>
            <td  style="font-size: 110%" align="right" height="30px">
                <p> ${.now?string("yyyy")}年${.now?string("mm")}月${.now?string("dd")}日</p>
            </td>
        </tr>
        <tr>
            <td style="font-size: 110%" width="35%" height="60px">

            </td>
            <td>

            </td>
        </tr>
        <tr>
            <td style="font-size: 110%" width="35%" height="30px">
                建设单位签收：
            </td>
            <td>

            </td>
        </tr>

        <tr>
            <td>

            </td>
            <td style="font-size: 110%" align="right" height="30px">
                <p>年 &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;月&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;日&nbsp; &nbsp; &nbsp; &nbsp;</p>
            </td>
        </tr>

        <tr>
            <td colspan="2" style="font-size: 110%" align="center" height="30px">
                <p>一式二份，一份交建设单位，一份存档。</p>
            </td>
        </tr>
    </table>
<#--    <p>jfreechart</p>-->
<#--    <div>-->
<#--        <img src="${scatterUrl}" alt="散点图" width="700" height="600"/>-->
<#--    </div>-->
</div>

<#--<span style="page-break-after:always;"></span>-->
<#--<div class="page">-->
<#--    -->
<#--</div>-->


</body>

</html>
