package net.diaowen.dwsurvey.controller.survey;

import net.diaowen.common.base.entity.User;
import net.diaowen.common.base.service.AccountManager;
import net.diaowen.common.plugs.httpclient.HttpResult;
import net.diaowen.common.plugs.httpclient.PageResult;
import net.diaowen.common.plugs.httpclient.ResultUtils;
import net.diaowen.common.plugs.page.Page;
import net.diaowen.common.utils.UserAgentUtils;
import net.diaowen.common.utils.ZipUtil;
import net.diaowen.dwsurvey.config.DWSurveyConfig;
import net.diaowen.dwsurvey.entity.*;
import net.diaowen.dwsurvey.service.AnUploadFileManager;
import net.diaowen.dwsurvey.service.SurveyAnswerManager;
import net.diaowen.dwsurvey.service.SurveyDirectoryManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/dwsurvey/app/answer")
public class MySurveyAnswerController {

    private final SurveyDirectoryManager surveyDirectoryManager;
    private final SurveyAnswerManager surveyAnswerManager;
    private final AccountManager accountManager;
    private final AnUploadFileManager anUploadFileManager;

    /**
     * 日志
     */
    private final Logger logger = Logger.getLogger(MySurveyAnswerController.class.getName());

    @Autowired
    public MySurveyAnswerController(SurveyDirectoryManager surveyDirectoryManager, SurveyAnswerManager surveyAnswerManager, AccountManager accountManager, AnUploadFileManager anUploadFileManager) {
        this.surveyDirectoryManager = surveyDirectoryManager;
        this.surveyAnswerManager = surveyAnswerManager;
        this.accountManager = accountManager;
        this.anUploadFileManager = anUploadFileManager;
    }

    /**
     * 获取答卷列表
     * @return 问卷列表
     */
    @GetMapping(path = "/list.do")
    public PageResult<SurveyAnswer> survey(HttpServletRequest request,PageResult<SurveyAnswer> pageResult, String surveyId,String ipAddr,String city,Integer isEffective) {
        UserAgentUtils.userAgent(request);
        User user = accountManager.getCurUser();
        if(user!=null){
            Page<SurveyAnswer> page = ResultUtils.getPageByPageResult(pageResult);
            SurveyDirectory survey=surveyDirectoryManager.get(surveyId);
            if(survey!=null){
                if(!user.getId().equals(survey.getUserId())){
                    pageResult.setSuccess(false);
                    return pageResult;
                }
                page=surveyAnswerManager.answerPage(page, surveyId);
            }
            pageResult = ResultUtils.getPageResultByPage(page,pageResult);
        }
        return pageResult;
    }

    /**
     * 根据当前登录用户，获取当前登录用户所填写过的所有实名问卷
     *
     * @param pageResult 封装了答卷分页查询结果的对象
     * @return 所有该用户填写过的实名问卷列表
     */
    @GetMapping(path = "/answered-list.do")
    public PageResult<SurveyAnswer> getSurveyResultByUserId(PageResult<SurveyAnswer> pageResult){
        // 获取当前系统的登录用户
        User user = accountManager.getCurUser();
        if (user != null){
            Page<SurveyAnswer> page = ResultUtils.getPageByPageResult(pageResult);
            // 查询该用户的回答过的所有答卷
            page = surveyAnswerManager.getAnswerPageByUserId(page, user.getId());
            pageResult = ResultUtils.getPageResultByPage(page, pageResult);
        }

        return pageResult;
    }

    /**
     * 根据答卷填写用户的ID获取该用户的个人信息
     *
     * @param id 用户ID
     * @return 用户个人信息
     */
    @PostMapping(path = "/user-info.do")
    @ResponseBody
    public UserInfoForm getAnswerInfo(@RequestParam String id){
        User user = accountManager.getUser(id);
        // 构造用户个人信息的表单对象
        UserInfoForm userInfoForm = new UserInfoForm(user.getId(), user.getLoginName(), user.getBirthday(),
                user.getSex(), user.getEmail(), user.getCellphone());

        return userInfoForm; // 返回表单对象到前端展示用户基本信息
    }

    /**
     * 根据回答 id 返回包含回答和问题的问卷
     * @param answerId 回答id
     * @return  是否查找成功
     * @throws Exception
     */
    @GetMapping(path = "/info.do")
    public HttpResult info(String answerId) {
        try {
            SurveyAnswer answer = null;
            // 当字符串 answerId 不为空时，获取 answer
            if (StringUtils.isNotEmpty(answerId)) {
                answer = surveyAnswerManager.findById(answerId);
            }
            if(answer!=null){
                //借由 回答 获取问卷
                SurveyDirectory survey = surveyDirectoryManager.findUniqueBy(answer.getSurveyId());
                User user= accountManager.getCurUser();
                if(user!=null && survey!=null) {
                    //检查问卷是否属于当前用户
                    if(!user.getId().equals(survey.getUserId())) {
                        return HttpResult.FAILURE_MSG("没有相应数据权限");
                    }
                    List<Question> questions = surveyAnswerManager.findAnswerDetail(answer);
                    survey.setQuestions(questions);
                    survey.setSurveyAnswer(answer);
                    return HttpResult.SUCCESS(survey);
                }
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return HttpResult.FAILURE();
    }


    /**
     * 删除所有给定 id 的问卷回答
     * @return
     * @throws Exception
     */
    @DeleteMapping(path = "/delete.do")
    public HttpResult delete(@RequestBody Map<String, String[]> map) {
        try{
                if(map!=null && map.containsKey("id")){
                    String[] ids = map.get("id");
                    if(ids!=null){
                        surveyAnswerManager.deleteData(ids);
                    }
                }
            return HttpResult.SUCCESS();
        }catch (Exception e){
           logger.warning(e.getMessage());
        }
        return HttpResult.FAILURE();
    }


    /**
     * 导出问卷为 xls
     * @param request 请求
     * @param response 响应
     * @param surveyId 问卷id
     * @param expUpQu
     * @return
     */
    @RequestMapping("/export-xls.do")
    public String exportXLS(HttpServletRequest request, HttpServletResponse response, String surveyId, String expUpQu) {
        try{
            String savePath = DWSurveyConfig.DWSURVEY_WEB_FILE_PATH;
            User user=accountManager.getCurUser();
            SurveyDirectory survey = surveyDirectoryManager.get(surveyId);
                if(user != null && survey != null){
                    if(!user.getId().equals(survey.getUserId())){
                        return "没有相应数据权限";
                    }
                    List<AnUplodFile> anUplodFiles = anUploadFileManager.findAnswer(surveyId);
                    if(anUplodFiles!=null && !anUplodFiles.isEmpty() && "1".equals(expUpQu)){
                        //直接导出excel，不存在上传文件的问题
                        surveyAnswerManager.exportXLS(surveyId,savePath,true);
                        //启用压缩导出
                        String fromPath =DWSurveyConfig.DWSURVEY_WEB_FILE_PATH+"/webin/expfile/"+surveyId;
                        fromPath = fromPath.replace("/", File.separator);

                        String zipPath = DWSurveyConfig.DWSURVEY_WEB_FILE_PATH+"/webin/zip/".replace("/", File.separator);
                        File file = new File(zipPath);
                        if (!file.exists()) file.mkdirs();

                        String toPath = zipPath+surveyId + ".zip";
                        toPath = toPath.replace("/",File.separator);
                        ZipUtil.createZip(fromPath, toPath, false);
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("dwsurvey_"+survey.getSid()+".zip", "UTF-8"));
                        request.getRequestDispatcher("/webin/zip/"+ surveyId + ".zip").forward(request,response);
                    }else{
                        //直接导出excel，不存在上传文件的问题
                        savePath=surveyAnswerManager.exportXLS(surveyId,savePath, false);
                        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("dwsurvey_"+survey.getSid()+".xlsx", "UTF-8"));
                        request.getRequestDispatcher(savePath).forward(request,response);
                    }
                }
        }catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return null;
    }


}
