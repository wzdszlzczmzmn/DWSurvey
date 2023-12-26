package net.diaowen.dwsurvey.controller.question;

import com.octo.captcha.service.image.ImageCaptchaService;
import net.diaowen.common.QuType;
import net.diaowen.common.base.entity.User;
import net.diaowen.common.base.service.AccountManager;
import net.diaowen.common.plugs.httpclient.HttpResult;
import net.diaowen.common.plugs.ipaddr.IPService;
import net.diaowen.common.plugs.web.Token;
import net.diaowen.common.plugs.zxing.ZxingUtil;
import net.diaowen.common.utils.CookieUtils;
import net.diaowen.common.utils.NumberUtils;
import net.diaowen.dwsurvey.common.AnswerCheckData;
import net.diaowen.dwsurvey.config.DWSurveyConfig;
import net.diaowen.dwsurvey.entity.*;
import net.diaowen.dwsurvey.service.SurveyAnswerManager;
import net.diaowen.dwsurvey.service.SurveyDirectoryManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 回答问卷的Controller，实现问卷回答结果的有效性验证、解析和保存、问卷详细信息JSON文件的获取、问卷填写的二维码的生成
 *
 * @author KeYuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
@Controller
@RequestMapping("/api/dwsurvey/anon/response")
public class ResponseController {
	private static final long serialVersionUID = -2289729314160067840L;

	/**
	 * 日志
	 */
	private final Logger logger = Logger.getLogger(ResponseController.class.getName());
	/**
	 * 问卷回答相关业务逻辑的服务组件接口,由Spring自动装配
	 */
	@Autowired
	private SurveyAnswerManager surveyAnswerManager;
	/**
	 * 问卷相关业务逻辑的服务组件接口,由Spring自动装配
	 */
	@Autowired
	private SurveyDirectoryManager directoryManager;
	/**
	 * IP相关业务逻辑的组件接口,由Spring自动装配
	 */
	@Autowired
	private IPService ipService;
	/**
	 * 用户相关业务逻辑服务组件接口,由Spring自动装配
	 */
	@Autowired
	private AccountManager accountManager;
	/**
	 * 图片验证码相关业务逻辑的服务组件接口,由Spring自动装配
	 */
	@Autowired
	private ImageCaptchaService imageCaptchaService;

	/**
	 * 保存问卷填写结果的接口
	 *
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param surveyId 填写的问卷的ID
	 * @return 问卷填写的结果页面的重定向路径
	 */
	@RequestMapping("/save.do")
	public String save(HttpServletRequest request,HttpServletResponse response,String surveyId) throws Exception {
		return saveSurvey(request,response,surveyId);
	}

	/**
	 * 移动端保存问卷填写结果的接口
	 *
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param surveyId 填写的问卷的ID
	 * @return 问卷填写的结果页面的重定向路径
	 */
	@RequestMapping("/saveMobile.do")
	public String saveMobile(HttpServletRequest request,HttpServletResponse response,String surveyId) throws Exception {
		return saveSurvey(request,response,surveyId);
	}

	/**
	 * 对问卷填写结果进行保存的方法,涉及问卷设置的验证和结果的保存
	 *
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param surveyId 填写的问卷的ID
	 * @return 问卷填写的结果页面的重定向路径
	 */
	public String saveSurvey(HttpServletRequest request,HttpServletResponse response,String surveyId) throws Exception {
		// 初始化问卷对象
		SurveyDirectory directory = null;
		try {
			// 根据问卷ID获取对应的问卷对象
			directory = directoryManager.getSurvey(surveyId);
			// 初始化问卷回答对象
			SurveyAnswer entity = new SurveyAnswer();
			// 调用相关方法获取问卷的相关设置,判断填写结果是否有效
			AnswerCheckData answerCheckData = answerCheckData(request,directory, true, entity);
			// 若该问卷填写结果无效,重定向到问卷填写的结果页面,返回相应无效状态码
			if(!answerCheckData.isAnswerCheck()) return answerRedirect(directory,answerCheckData.getAnswerCheckCode());
			// 保存问卷填写的结果
			answerSurvey(request,surveyId,entity);
			// 在该问卷填写结果保存后更新问卷的状态信息
			answerAfterUpData(request,response,surveyId,entity.getId());
			// 重定向到问卷填写的结果页面
			return answerRedirect(directory,6, entity.getId());
		} catch (Exception e) {
			logger.warning(e.getMessage());
			// 重定向到问卷的填写结果页面,状态码表示填写出错
			return answerRedirect(directory,5);
		}
	}

	/**
	 * 获取问卷的相关设置,对填写结果是否有效进行校验
	 *
	 * @param request HTTP请求
	 * @param directory 被填写的问卷对象
	 * @param isSubmit 是否提交
	 * @param entity 问卷答案对象
	 * @return 填写结果有效性校验的结果
	 */
	public AnswerCheckData answerCheckData(HttpServletRequest request, SurveyDirectory directory,boolean isSubmit,SurveyAnswer entity) {
		// 初始化问卷填写是否有效的检查对象
		AnswerCheckData answerCheckData = new AnswerCheckData();
		String surveyId = directory.getId();
		// 获取问卷配置的详细信息对象
		SurveyDetail surveyDetail = directory.getSurveyDetail();
		// 问卷已填写的数量
		Integer answerNum = directory.getAnswerNum();
		// 问卷的可见性
		Integer visibility = directory.getVisibility();

		// 获取问卷的有效性限制
		Integer effective = surveyDetail.getEffective();
		// 获取问卷的调查规则,是否使用令牌、公开或私有
		Integer rule = surveyDetail.getRule();
		// 获取问卷是否是实名问卷的设置
		Integer isRealName = surveyDetail.getIsRealName();
		// 获取防刷新设置是否启用
		Integer refresh = surveyDetail.getRefresh();
		// 获取防刷新次数限制设置
		Integer refreshNum = surveyDetail.getRefreshNum();
		// 是否根据收集到的问卷份数结束收集
		Integer ynEndNum = surveyDetail.getYnEndNum();
		// 问卷截止收集所需收集到的问卷份数
		Integer endNum = surveyDetail.getEndNum();
		// 是否根据设定的结束时间结束问卷的收集
		Integer ynEndTime = surveyDetail.getYnEndTime();
		// 问卷收集的结束时间
		Date endTime = surveyDetail.getEndTime();

		// 验证问卷的可见性
		if(visibility!=1){
			answerCheckData.setAnswerCheck(false);
			answerCheckData.setAnswerCheckCode(10);//问卷已经删除
			return answerCheckData;
		}

		// 验证问卷是否开启
		if (directory.getSurveyQuNum()<=0 || directory.getSurveyState() != 1 ) {
			answerCheckData.setAnswerCheck(false);
			answerCheckData.setAnswerCheckCode(1);//问卷未开启
			return answerCheckData;
		}

		// 对于问卷设置"每台电脑或手机只能答一次"进行验证, 采用cookie
		Cookie cookie = CookieUtils.getCookie(request, surveyId);
		Integer cookieAnNum = 0;
		if(effective!=null && effective>1 && cookie!=null){
			//effective cookie
			String cookieValue = cookie.getValue();
			// 用正则表达式检查cookie是否为纯数字
			if (cookieValue != null && NumberUtils.isNumeric(cookieValue)) {
				cookieAnNum = Integer.parseInt(cookieValue);
			}
			// 存在重复回答,验证失败
			if(cookieAnNum>0){
				answerCheckData.setAnswerCheck(false);
				answerCheckData.setAnswerCheckCode(3);//超过cookie次数限制
				//跳转到结束提示
				return answerCheckData;
			}
		}

		String ip = ipService.getIp(request);
		// 验证问卷设置"每个IP只能答一次"
		Integer effectiveIp = surveyDetail.getEffectiveIp(); // 获取问卷是否设置IP验证
		if (effectiveIp != null && effectiveIp == 1) {
			// 获取该IP的问卷填写数量
			Long ipAnNum = surveyAnswerManager.getCountByIp(surveyId, ip);
			// 该IP已回答过问卷,校验失败
			if(ipAnNum!=null && ipAnNum > 0){
				answerCheckData.setAnswerCheck(false);
				answerCheckData.setAnswerCheckCode(23);//超过单个IP次数限制
				return answerCheckData;
			}
		}



		String ruleCode = request.getParameter("ruleCode");
		// 验证问卷设置"拥有口令密码才可以答题"
		if(rule!=null && rule==3){
			boolean isPwdCode = false;
			// 填写令牌
			if(StringUtils.isNotEmpty(ruleCode)) {
				// 验证令牌是否正确
				if(!ruleCode.equals(surveyDetail.getRuleCode())){
					answerCheckData.setAnswerCheck(false);
					answerCheckData.setAnswerCheckCode(302);//口令错误
					return answerCheckData;
				}
			}
			// 未填写令牌
			if(StringUtils.isEmpty(ruleCode)) {
				answerCheckData.setAnswerCheck(false);
				answerCheckData.setAnswerCheckCode(303);//口令为空
				return answerCheckData;
			}
		}


		// 验证问卷设置"问卷收集根据截止时间截止"
		if(endTime!=null && ynEndTime==1 &&  (new Date().getTime()-endTime.getTime()) > 0 ){
			// 设置问卷收集结束
			directory.setSurveyState(2);
			// 更新问卷信息,将问卷结束修改更新到数据库
			directoryManager.saveByAdmin(directory);
			answerCheckData.setAnswerCheck(false);
			answerCheckData.setAnswerCheckCode(9);
			return answerCheckData;
		}

		// 验证问卷设置"根据收集到的问卷数量结束收集"
		if(answerNum!=null && ynEndNum==1 && answerNum >= endNum ){
			answerCheckData.setAnswerCheck(false);
			answerCheckData.setAnswerCheckCode(7);
			return answerCheckData;
		}

		// 验证是否有重复回答以启用验证码, 根据cookie判断
		if(refresh!=null && refresh==1){
			Cookie refCookie = CookieUtils.getCookie(request, "r_"+surveyId);
			if (refCookie!=null) {
				//cookie启用验证码
				answerCheckData.setImgCheckCode(true);
			}
		}


		HttpSession httpSession = request.getSession();

		// 在启用验证码的情况下,对验证填写是否正确进行校验
		if(isSubmit){
			String refCookieKey = "r_"+surveyId;
			// 获取refCookieKey所对应的Cookie
			Cookie refCookie = CookieUtils.getCookie(request, refCookieKey);
			// 校验防刷新是否启用和refCookie是否为空,防刷新启用且refCookie不为空时执行后续操作
			if (( refresh==1 && refCookie!=null)) {
				// 获取请求参数
				String code = request.getParameter("jcaptchaInput");
				// 验证用户输入的验证码是否正确
				if (!imageCaptchaService.validateResponseForID(request.getSession().getId(), code)) {
//					return "redirect:/response/answer-dwsurvey.do?surveyId="+surveyId+"&redType=4";
//				return answerRedirect(directory,4);
					answerCheckData.setAnswerCheck(false);
					answerCheckData.setAnswerCheckCode(4);
					return answerCheckData;
				}
			}

			if (isRealName == 1){ // 实名问卷，记录填写用户的信息
				// 获取当前登录用户
				User user = accountManager.getCurUser();
				if (user != null) {
					// 设置问卷填写的用户
					entity.setUserId(user.getId());
				}else {
					answerCheckData.setAnswerCheck(false);
					answerCheckData.setAnswerCheckCode(11); // 问卷为实名问卷，用户未登录回答无效
					return answerCheckData;
				}
			}

			// 设置问卷提交的IP地址
			entity.setIpAddr(ip);
			// 设置该填写结果对应的问卷ID
			entity.setSurveyId(surveyId);
		}
		return answerCheckData;
	}

	/**
	 * 保存问卷填写的结果
	 *
	 * @param request HTTP请求
	 * @param surveyId 填写的问卷的ID
	 * @param entity 问卷答案实体
	 */
	public void answerSurvey(HttpServletRequest request,String surveyId,SurveyAnswer entity){
		//
		Map<String, Map<String, Object>> quMaps = buildSaveSurveyMap(request);
		String bgTimeAttrName = "bgTime"+surveyId;
		// 获取问卷填写的开始时间
		Date bgAnTime = (Date)request.getSession().getAttribute(bgTimeAttrName);
		// 设置问卷回答实体的问卷填写开始时间
		entity.setBgAnDate(bgAnTime);
		// 设置问卷回答实体的问卷填写结束时间
		entity.setEndAnDate(new Date());
		// 保存问卷填写的结果
		surveyAnswerManager.saveAnswer(entity, quMaps);
	}

	/**
	 * 问卷填写结果保存后更新问卷的信息
	 *
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param surveyId 问卷ID
	 * @param answerId 问卷填写结果ID
	 */
	public void answerAfterUpData(HttpServletRequest request, HttpServletResponse response, String surveyId,String answerId) {
		SurveyDirectory directory = directoryManager.getSurvey(surveyId);
		// 获取问卷设置详细信息
		SurveyDetail surveyDetail = directory.getSurveyDetail();
		// 问卷的回答次数
		Integer answerNum = directory.getAnswerNum();
		// 是否根据收集到的问卷份数结束收集
		Integer ynEndNum = surveyDetail.getYnEndNum();
		// 问卷截止收集所需收集到的问卷份数
		Integer endNum = surveyDetail.getEndNum();
		// 是否根据设定的结束时间结束问卷的收集
		Integer ynEndTime = surveyDetail.getYnEndTime();
		// 问卷的结束时间
		Date endTime = surveyDetail.getEndTime();

		// 问卷有效性的间隔时间
		int effe = surveyDetail.getEffectiveTime();
		effe = 24;
		String refCookieKey = "r_"+surveyId;
		// 添加问卷填写的cookie信息
		CookieUtils.addCookie(response, surveyId, (1) + "",effe * 60, "/");
		CookieUtils.addCookie(response, refCookieKey, (1) + "", null, "/");

		// 检查问卷是否由于截止时间而结束收集
		if(endTime!=null && ynEndTime==1 &&  (new Date().getTime()-endTime.getTime()) > 0 ){
			directory.setSurveyState(2);
			directoryManager.saveByAdmin(directory);
		}

		// 检查问卷是否由于提交份数而截止收集
		if(answerNum!=null && ynEndNum==1 && answerNum >= endNum ){
			directory.setSurveyState(2);
			directoryManager.saveByAdmin(directory);
		}
	}

	/**
	 * 构造问卷填写结果的Map
	 *
	 * @param request HTTP请求
	 * @return 问卷填写结果的Map
	 */
	public Map<String, Map<String, Object>> buildSaveSurveyMap(HttpServletRequest request) {
		// 初始化问卷填写结果Map
		Map<String, Map<String, Object>> quMaps = new HashMap<>();

		// 获取是非题的填写结果Map
		Map<String, Object> yesnoMaps = WebUtils.getParametersStartingWith(
				request, "qu_" + QuType.YESNO + "_");
		// 将是非题填写结果添加到问卷填写结果Map
		quMaps.put("yesnoMaps", yesnoMaps);

		// 获取单选题的填写结果Map
		Map<String, Object> radioMaps = WebUtils.getParametersStartingWith(
				request, "qu_"+QuType.RADIO + "_");

		// 获取多选题的填写结果Map
		Map<String, Object> checkboxMaps = WebUtils.getParametersStartingWith(
				request, "qu_"+QuType.CHECKBOX + "_");

		// 获取填空题的填写结果Map
		Map<String, Object> fillblankMaps = WebUtils.getParametersStartingWith(
				request, "qu_" + QuType.FILLBLANK + "_");
		//将填空题填写结果添加到问卷填写结果Map中
		quMaps.put("fillblankMaps", fillblankMaps);

		// 获取多项填空题的填写结果Map
		Map<String, Object> dfillblankMaps = WebUtils
				.getParametersStartingWith(request, "qu_"
						+ QuType.MULTIFILLBLANK + "_");
		// 获取多项填空题每一题的填写结果
		for (String key : dfillblankMaps.keySet()) {
			String dfillValue = dfillblankMaps.get(key).toString();
			Map<String, Object> map = WebUtils.getParametersStartingWith(
					request, dfillValue);
			// 将每一题的填写结果写入多项填空题的Map中
			dfillblankMaps.put(key, map);
		}
		// 将多项填空题的填写结果写入问卷填写结果Map中
		quMaps.put("multifillblankMaps", dfillblankMaps);

		// 获取多行填空题的填写结果
		Map<String, Object> answerMaps = WebUtils.getParametersStartingWith(
				request, "qu_" + QuType.ANSWER + "_");
		// 将多行填空题的填写结果添加到问卷填写结果Map中
		quMaps.put("answerMaps", answerMaps);

		// 获取复合单选题的填写结果Map
		Map<String, Object> compRadioMaps = WebUtils.getParametersStartingWith(
				request, "qu_" + QuType.COMPRADIO + "_");
		for (String key : compRadioMaps.keySet()) {
			String quItemId = compRadioMaps.get(key).toString();
			String otherText = request.getParameter("text_qu_"
					+ QuType.COMPRADIO + "_" + key + "_" + quItemId);
			AnRadio anRadio = new AnRadio();
			anRadio.setQuId(key);
			anRadio.setQuItemId(quItemId);
			anRadio.setOtherText(otherText);
			compRadioMaps.put(key, anRadio);
		}
		// 将复合单选题的选择结果写入问卷填写结果Map
		quMaps.put("compRadioMaps", compRadioMaps);

		// 获取复合多选题的填写结果Map
		Map<String, Object> compCheckboxMaps = WebUtils
				.getParametersStartingWith(request, "qu_" + QuType.COMPCHECKBOX
						+ "_");//复合多选
		for (String key : compCheckboxMaps.keySet()) {
			String dfillValue = compCheckboxMaps.get(key).toString();
			Map<String, Object> map = WebUtils.getParametersStartingWith(
					request, "tag_" + dfillValue);
			for (String key2 : map.keySet()) {
				String quItemId = map.get(key2).toString();
				String otherText = request.getParameter("text_"
						+ dfillValue + quItemId);
				AnCheckbox anCheckbox = new AnCheckbox();
				anCheckbox.setQuItemId(quItemId);
				anCheckbox.setOtherText(otherText);
				map.put(key2, anCheckbox);
			}
			compCheckboxMaps.put(key, map);
		}
		// 将复合多选题的填写结果写入填写结果Map
		quMaps.put("compCheckboxMaps", compCheckboxMaps);

		// 获取枚举题填写结果的Map
		Map<String, Object> enumMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.ENUMQU + "_");//枚举
		// 将枚举题的填写结果Map写入问卷填写结果Map中
		quMaps.put("enumMaps", enumMaps);

		// 获取排序题的填写结果的Map
		Map<String, Object> quOrderMaps = WebUtils.getParametersStartingWith(
				request, "qu_" + QuType.ORDERQU + "_");//排序
		// 获取排序题每一题的选择结果
		for (String key : quOrderMaps.keySet()) {
			String tag = quOrderMaps.get(key).toString();
			Map<String, Object> map = WebUtils.getParametersStartingWith(
					request, tag);
			// 将每一题的选择结果写入排序题填写结果Map
			quOrderMaps.put(key, map);
		}
		// 将排序题填写结果Map写入问卷填写Map中
		quMaps.put("quOrderMaps", quOrderMaps);

		// 获取单选题每一题选择结果
		for (String key:radioMaps.keySet()) {
			String quItemId = radioMaps.get(key).toString();
			String otherText = request.getParameter("text_qu_"
					+ QuType.RADIO + "_" + key + "_" + quItemId);
			AnRadio anRadio = new AnRadio();
			anRadio.setQuId(key);
			anRadio.setQuItemId(quItemId);
			anRadio.setOtherText(otherText);
			radioMaps.put(key, anRadio);
		}

		// 获取评分题的填写结果Map
		Map<String, Object> scoreMaps = WebUtils.getParametersStartingWith(
				request, "qu_" + QuType.SCORE + "_");
		// 获取评分题每一项的评分结果
		for (String key : scoreMaps.keySet()) {
			String tag = scoreMaps.get(key).toString();
			Map<String, Object> map = WebUtils.getParametersStartingWith(
					request, tag);
			// 将评分题每一项的评分结果写入到评分题填写结果Map中
			scoreMaps.put(key, map);
		}
		// 将评分题填写结果Map写入到问卷填写结果Map中
		quMaps.put("scoreMaps", scoreMaps);
		// 将选择题填写结果Map写入到问卷填写结果Map中
		quMaps.put("compRadioMaps", radioMaps);
		// 获取多选题每一题的选择结果
		for (String key : checkboxMaps.keySet()) {
			String dfillValue = checkboxMaps.get(key).toString();
			Map<String, Object> map = WebUtils.getParametersStartingWith(
					request, dfillValue);
			// 获取多选题每一项的选择结果
			for (String key2 : map.keySet()) {
				String quItemId = map.get(key2).toString();
				String otherText = request.getParameter("text_"
						+ dfillValue + quItemId);
				AnCheckbox anCheckbox = new AnCheckbox();
				anCheckbox.setQuItemId(quItemId);
				anCheckbox.setOtherText(otherText);
				map.put(key2, anCheckbox);
			}
			checkboxMaps.put(key, map);
		}
		// 将多选题的选择结果写入到问卷填写结果Map中
		quMaps.put("compCheckboxMaps", checkboxMaps);

		// 获取文件上传题的填写结果Map
		Map<String, Object> uploadFileMaps = WebUtils.getParametersStartingWith(
				request, "qu_" + QuType.UPLOADFILE + "_");//填空
		// 将文件上传题的填写结果写入到问卷填写结果Map中
		quMaps.put("uploadFileMaps", uploadFileMaps);

		// 返回问卷填写结果Map
		return quMaps;
	}

	/**
	 * 重定向到问卷填写结果页面
	 *
	 * @param directory 被填写的问卷对象
	 * @param redType 填写结果状态码
	 * @return 调用另一个重定向函数，重定向到问卷提交结果页面
	 */
	public String answerRedirect(SurveyDirectory directory, int redType) throws Exception {
		return answerRedirect(directory,redType,null);
	}

	/**
	 * 重定向到问卷填写结果页面
	 *
	 * @param directory 被填写的问卷对象
	 * @param redType 填写结果状态码
	 * @param answerId 问卷填写结果的ID
	 * @return 重定向到问填写结果的页面
	 */
	public String answerRedirect(SurveyDirectory directory, int redType, String answerId) throws Exception {
		if(directory!=null){
			return "redirect:"+DWSurveyConfig.DWSURVEY_WEB_SITE_URL+"/static/diaowen/message.html?sid="+directory.getSid()+"&respType="+redType+"&answerId="+answerId;
		}
		return "redirect:"+DWSurveyConfig.DWSURVEY_WEB_SITE_URL+"/static/diaowen/message.html";
	}

	/**
	 * 获取包含问卷对象和状态码的视图对象
	 *
	 * @param request HTTP请求
	 * @param surveyId 问卷ID
	 * @param redType 状态码
	 * @return 包含问卷对象和状态码的视图
	 */
	@RequestMapping("/answer-dwsurvey.do")
	public ModelAndView answerRedirect(HttpServletRequest request,String surveyId, int redType) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		// 获取问卷对象
		SurveyDirectory directory = directoryManager.getSurvey(surveyId);
		// 将问卷对象添加到Model
		modelAndView.addObject("survey", directory);
		// 将
		modelAndView.addObject("redType", redType);
		modelAndView.setViewName("/diaowen-answer/response-msg-1");
		return modelAndView;
	}

	/**
	 * 答卷异步有效性验
	 *
	 * @return 检查结果
	 */
	@RequestMapping("/check-answer-survey.do")
	@ResponseBody
	public HttpResult checkAnswerSurvey(HttpServletRequest request,HttpServletResponse response,String surveyId){
		//0、token 访止重复提交
		String token = Token.getToken(request);
		AnswerCheckData answerCheckData = answerCheckData(request,surveyId);
		answerCheckData.setToken(token);
		return HttpResult.SUCCESS(answerCheckData);
	}

	/**
	 * 检查问卷填写的有效性
	 *
	 * @param request HTTP请求
	 * @param surveyId 问卷ID
	 * @return 验证的结果
	 */
	public AnswerCheckData answerCheckData(HttpServletRequest request, String surveyId){
		SurveyDirectory directory = directoryManager.getSurvey(surveyId);
		return answerCheckData(request,directory, false, null);
	}


	/**
	 * 当sid为空时,根据surveyId获取问卷对象从而获取问卷详情
	 *
	 * @return 重定向转发请求保存问卷详细信息的JSON文件
	 */
	@RequestMapping(value = "/survey.do")
	public String survey(HttpServletRequest request, HttpServletResponse response, String sid,String surveyId) {
		try {
			// 判断sid为空且surveyId不为空
			if(StringUtils.isEmpty(sid) && StringUtils.isNotEmpty(surveyId)){
				// 获取surveyId对应的问卷对象
				SurveyDirectory surveyDirectory = directoryManager.get(surveyId);
				// 设置sid
				sid = surveyDirectory.getSid();
			}
			// 获取问卷详细信息的json文件的地址
			String jsonPath = "/file/survey/"+sid+"/"+sid+".json";
			// 判断问卷的详细信息json文件是否存在
			surveyJsonExists(sid, jsonPath);
			// 转发请求
			request.getRequestDispatcher(jsonPath).forward(request,response);
		} catch (ServletException | IOException e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据sid获取问卷的详细信息
	 *
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param sid 问卷的sid
	 * @return 重定向转发请求保存问卷详细信息的JSON文件
	 */
	@RequestMapping(value = "/survey_info.do")
	public String surveyInfo(HttpServletRequest request, HttpServletResponse response, String sid) {
		// 获取问卷详细信息的json文件的地址
		String jsonPath = "/file/survey/"+sid+"/"+sid+"_info.json";
		try {
			// 判断JSON问卷是否存在
			surveyJsonExists(sid, jsonPath);
			// 转发请求
			request.getRequestDispatcher(jsonPath).forward(request,response);
		} catch (ServletException | IOException e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * 判断问卷详细信息的Json串是否存在
	 *
	 * @param sid 问卷的sid
	 * @param jsonPath 问卷的json文件地址
	 */
	private void surveyJsonExists(String sid, String jsonPath) {
		//判断有无没有则生成一个
		String filePath = DWSurveyConfig.DWSURVEY_WEB_FILE_PATH+ jsonPath;
		filePath = filePath.replace("/",File.separator);
		filePath = filePath.replace("\\",File.separator);
		File file = new File(filePath);
		if(!file.exists()){
			//不存在则生成一个
			SurveyDirectory directory = directoryManager.getSurveyBySid(sid);
			// 生成对应问卷详细信息的Json串
			directoryManager.devSurveyJson(directory.getId());
		}
	}

	/**
	 * 生成问卷的填写二维码
	 *
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param surveyId 问卷ID
	 * @param sid 问卷的sid
	 * @return 生成的问卷填写二维码
	 */
	@RequestMapping("/answerTD.do")
	public String answerTD(HttpServletRequest request,HttpServletResponse response,String surveyId,String sid) throws Exception{
		String WEB_SITE_URL = DWSurveyConfig.DWSURVEY_WEB_SITE_URL;
		String down=request.getParameter("down");
		String ruleCode = request.getParameter("ruleCode");
		String baseUrl = "";
		baseUrl = request.getScheme() +"://" + request.getServerName()
				+ (request.getServerPort() == 80 ? "" : ":" +request.getServerPort())
				+ request.getContextPath();
		baseUrl = WEB_SITE_URL;
//		String encoderContent= baseUrl+"/response/answerMobile.do?surveyId="+surveyId;
		String encoderContent = null;
		if(StringUtils.isNotEmpty(surveyId)){
			encoderContent = baseUrl+"/static/diaowen/answer-m.html?surveyId="+surveyId;
		}
		if(StringUtils.isNotEmpty(sid)){
			encoderContent = baseUrl+"/static/diaowen/answer-m.html?sid="+sid;
		}
		if(StringUtils.isNotEmpty(ruleCode)){
			encoderContent+="&ruleCode="+ruleCode;
		}
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		BufferedImage twoDimensionImg = ZxingUtil.qRCodeCommon(encoderContent, "jpg", 16);
		ImageIO.write(twoDimensionImg, "jpg", jpegOutputStream);
		if(down==null){
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			ServletOutputStream responseOutputStream = response.getOutputStream();
			responseOutputStream.write(jpegOutputStream.toByteArray());
			responseOutputStream.flush();
			responseOutputStream.close();
		}else{
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(("diaowen_"+surveyId+".jpg").getBytes()));
			byte[] bys = jpegOutputStream.toByteArray();
			response.addHeader("Content-Length", "" + bys.length);
			ServletOutputStream responseOutputStream = response.getOutputStream();
			response.setContentType("application/octet-stream");
			responseOutputStream.write(bys);
			responseOutputStream.flush();
			responseOutputStream.close();
		}
		return null;
	}
}
