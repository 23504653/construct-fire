package cc.coopersoft.construct.fire.service;

import cc.coopersoft.common.cloud.schemas.WeedFsResult;
import cc.coopersoft.common.report.PdfReport;
import cc.coopersoft.construct.fire.common.EnumData;
import cc.coopersoft.construct.fire.model.FireCheck;
import cc.coopersoft.construct.fire.model.FireCheckInfo;
import cc.coopersoft.construct.fire.model.ProjectCorp;
import cc.coopersoft.construct.fire.model.Report;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

@Service
@Slf4j
public class ReportService {

    @Value("${report.mark:}")
    private String markText = "";

    @Value("${org.name:}")
    private String orgName = "";

    @Value("${org.word:}")
    private String orgWord = "";

    private final RemoteService remoteService;

    public ReportService(RemoteService remoteService){
        this.remoteService = remoteService;
    }


    private ProjectCorp getApplyCorp(FireCheck check){
        return check.getInfo().getProject().getCorps()
                .stream().filter(corp -> (corp.getCode() == check.getCorp()))
                .findFirst().orElseThrow();
    }

    private Map<String,Object> officialReportData(FireCheck check){
        Map<String,Object> data = new HashMap<>();
        data.put("fireCheck", check);
        data.put("org",orgName);
        data.put("word",orgWord);
        data.put("joinCorp",getApplyCorp(check));
        data.put("enumData", EnumData.values());
        return data;
    }

    private void addReport(FireCheck check, String name, boolean failReport, WeedFsResult weedFsResult){
        check.getReports().stream().filter(report -> report.getName().equals(name)).findFirst().ifPresent((r) -> check.getReports().remove(r));
        Report result = new Report(check, weedFsResult,
                name, failReport) ;
        check.getReports().add(result);
    }

    private void createOfficialLetter(FireCheck check, boolean failReport, String name, String template){
        log.debug("create report:" + name);
        addReport(check, name, failReport,remoteService.uploadTemplateReport(
                String.valueOf(check.getId()),
                markText, template,officialReportData(check)));
    }

    //建设工程消防验收申请不予受理通知书/  建设工程竣工验收消防备案材料补正通知书 /建设工程消防验收申请受理通知书  /建设工程竣工验收消防备案凭证（未抽中）/建设工程竣工验收消防备案凭证（抽中）
    public void applyResultReport(FireCheck fireCheck){

        if (FireCheck.Status.NoAccept.equals(fireCheck.getStatus())){
            if (fireCheck.getInfo().isSpecial()){
                createOfficialLetter(fireCheck,true,
                        "建设工程消防验收申请不予受理通知书","applyNot.ftl" );
            }else{
                createOfficialLetter(fireCheck, true,
                        "建设工程竣工验收消防备案材料补正通知书"
                        ,"correctionDate.ftl");
            }
        }else{
            if (fireCheck.getInfo().isSpecial()){
                createOfficialLetter(fireCheck,false,"建设工程消防验收申请受理通知书", "apply.ftl");
            }else{
                if (fireCheck.getInfo().isInRandom()){
                    createOfficialLetter(fireCheck,false,"建设工程竣工验收消防备案凭证（抽中）","filingCertificateSelect.ftl");
                }else
                    createOfficialLetter(fireCheck, false,"建设工程竣工验收消防备案凭证（未抽中）","filingCertificateNotSelect.ftl");
            }
        }

    }


    //建设工程消防验收意见书(验收失败)/建设工程消防验收意见书(验收成功)/建设工程竣工验收消防备案复查意见书/建设工程竣工验收消防备案检查意见书/建设工程竣工验收消防备案检查不合格通知书
    public void viewResultReport(FireCheck fireCheck){
        if (fireCheck.getInfo().isSpecial()){
            if (FireCheck.Status.Qualified.equals(fireCheck.getStatus())){
                createOfficialLetter(fireCheck, false,"建设工程消防验收意见书(验收成功)",
                        "fireControlAcceptance.ftl");
            }else if (FireCheck.Status.Unqualified.equals(fireCheck.getStatus())) {
                createOfficialLetter(fireCheck, true,"建设工程消防验收意见书(验收失败)",
                        "fireNotControlAcceptance.ftl");
            }else{
                throw new IllegalArgumentException("check not in status");
            }
        }else{
            if (FireCheck.Status.Qualified.equals(fireCheck.getStatus())){
                if (FireCheck.Type.First.equals(fireCheck.getType())){
                    createOfficialLetter(fireCheck, false,"建设工程竣工验收消防备案检查意见书"
                            ,"checkComment.ftl");
                }else if (FireCheck.Type.Review.equals(fireCheck.getType())){
                    createOfficialLetter(fireCheck, false,"建设工程竣工验收消防备案复查意见书"
                            ,"reviewComments.ftl");
                }else{
                    throw new IllegalArgumentException("type not in define");
                }
            }else if (FireCheck.Status.Unqualified.equals(fireCheck.getStatus())){
                createOfficialLetter(fireCheck, true,"建设工程竣工验收消防备案检查不合格通知书"
                        ,"unqualified.ftl");
            }else{
                throw new IllegalArgumentException("check not in status");
            }
        }
    }


    //建设工程消防验收申报表 / 建设工程竣工验收消防备案申报表
    public void applyReport(FireCheck fireCheck){
        if (fireCheck.getInfo().isSpecial()){
            createOfficialLetter(fireCheck, false,"建设工程消防验收申报表","acceptanceDeclarationForm.ftl");
        }else{
            createOfficialLetter(fireCheck,false,"建设工程竣工验收消防备案申报表","completedContractsRecodApply.ftl");
        }
    }


    //建设工程消防验收基本情况记录表 A4 需要横打
    public void applyItemsReport(FireCheck fireCheck){

        String name = "建设工程消防验收基本情况记录表";
        final RequestCallback requestCallback = request -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_PDF);
            Rectangle rectPageSize = new Rectangle(PageSize.A4);
            rectPageSize.rotate();
            PdfReport.sampleReport(rectPageSize,request.getBody(),
                    String.valueOf(fireCheck.getId()),
                    markText,"constructionFireRecord.ftl",
                    officialReportData(fireCheck));
        };

        addReport(fireCheck,name,false,remoteService.uploadReport(requestCallback));
    }

    // 测试报表
    public static void main(String[] args) {
        // MatrixToImageWriter.w


        FireCheckInfo fireCheck = new FireCheckInfo();


        FireCheck fc = new FireCheck();
        fc.setInfo(fireCheck);
        fc.setCorp(99l);

        Map<String,Object> data = new HashMap<>();
        data.put("fireCheck", fc);
        data.put("org","东港市住房和城乡建设局");
        List<String> testList = new ArrayList<>();
        testList.add("test1") ;
        testList.add("test2");
        data.put("testArray",testList);

        FileOutputStream fileOutputStream = null;
        try {

            //横版
            Rectangle rectPageSize = new Rectangle(PageSize.A4);
            rectPageSize.rotate();

            fileOutputStream = new FileOutputStream("/Users/cooper/Downloads/test.pdf");
            PdfReport.sampleReport(rectPageSize.rotate(),fileOutputStream,"ddd","东港市住房和城乡建设局","test.ftl", data);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

}

