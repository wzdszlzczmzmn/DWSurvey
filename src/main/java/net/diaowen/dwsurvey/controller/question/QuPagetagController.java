package net.diaowen.dwsurvey.controller.question;


import java.util.Map.Entry;
import net.diaowen.common.QuType;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.entity.QuestionLogic;
import net.diaowen.dwsurvey.service.QuestionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 分页题 action
 * @author KeYuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
@Controller
@RequestMapping("/api/dwsurvey/app/design/qu-pagetag")
public class QuPagetagController{
	/**
	 * 日志
	 */
	private final Logger logger = Logger.getLogger(QuPagetagController.class.getName());
	private final QuestionManager questionManager;

	@Autowired
	public QuPagetagController(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	/**
	 * 保存前端发来的分页标记（分页标记也是一个问题)
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ajaxSave.do")
	public String ajaxSave(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try{
			Question entity=ajaxBuildSaveOption(request); // 从请求中构造分页标记
			questionManager.save(entity);
			String resultJson=buildResultJson(entity);
			response.getWriter().write(resultJson);
		}catch (Exception e) {
			logger.warning(e.getMessage());
			response.getWriter().write("error");
		}
		return null;
	}

	// 从请求参数中获取信息，构造一个 Question (分页题）
	private Question ajaxBuildSaveOption(HttpServletRequest request) throws UnsupportedEncodingException {
		String quId=request.getParameter("quId");
		String belongId=request.getParameter("belongId");
		String quTitle=request.getParameter("quTitle");
		String orderById=request.getParameter("orderById");
		String tag=request.getParameter("tag");
		//isRequired 是否必选
		String isRequired=request.getParameter("isRequired");
		//hv 1水平显示 2垂直显示
		String hv=request.getParameter("hv");
		//randOrder 选项随机排列
		String randOrder=request.getParameter("randOrder");
		// 按列显示时的列数
		String cellCount=request.getParameter("cellCount");

		if("".equals(quId)){
			quId=null;
		}
		Question entity=questionManager.getModel(quId); // 创建一个空的分页标记
		// 设置所属的问卷或题库
		entity.setBelongId(belongId);
		if(quTitle!=null){
			// 处理 url 编码
			quTitle=URLDecoder.decode(quTitle,"utf-8");
			entity.setQuTitle(quTitle);
		}
		// 可能会抛出 NumberFormatException
		entity.setOrderById(Integer.parseInt(orderById));
		entity.setTag(Integer.parseInt(tag));
		// 设置问题类型为分页标记
		entity.setQuType(QuType.PAGETAG);
		isRequired=(isRequired==null || isRequired.isEmpty())?"0":isRequired;
		hv=(hv==null || hv.isEmpty())?"0":hv; // 水平或垂直显示
		randOrder=(randOrder==null || randOrder.isEmpty())?"0":randOrder; // 是否随机排列
		cellCount=(cellCount==null || cellCount.isEmpty())?"0":cellCount;	 // 按列显示时的列数

		entity.setIsRequired(Integer.parseInt(isRequired));
		entity.setHv(Integer.parseInt(hv));
		entity.setRandOrder(Integer.parseInt(randOrder));
		entity.setCellCount(Integer.parseInt(cellCount));

		// 获取所有以 quLogicId_ 开头的参数，构造跳转或显示逻辑
		Map<String, Object> quLogicIdMap=WebUtils.getParametersStartingWith(request, "quLogicId_");
		List<QuestionLogic> quLogics=new ArrayList<>();
		for (Entry<String, Object> entry : quLogicIdMap.entrySet()) {
			String key = entry.getKey();
			String cgQuItemId=request.getParameter("cgQuItemId_"+key); // 逻辑关联的选项
			String skQuId=request.getParameter("skQuId_"+key); // 逻辑关联的题目（分页标记）
			String visibility=request.getParameter("visibility_"+key);
			String logicType=request.getParameter("logicType_"+key);
			Object quLogicId=quLogicIdMap.get(key);
			String quLogicIdValue=(quLogicId!=null)?quLogicId.toString():null;

			QuestionLogic quLogic=new QuestionLogic();
			quLogic.setId(quLogicIdValue);
			quLogic.setCgQuItemId(cgQuItemId);
			quLogic.setSkQuId(skQuId);
			quLogic.setVisibility(Integer.parseInt(visibility));
			quLogic.setTitle(key);
			quLogic.setLogicType(logicType);
			quLogics.add(quLogic);
		}
		entity.setQuestionLogics(quLogics);

		return entity;
	}

	// 生成返回给前端的 json
	public static String buildResultJson(Question entity){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{id:'").append(entity.getId());// 题目 id
		stringBuilder.append("',orderById:");
		stringBuilder.append(entity.getOrderById()); // 题目序号
		stringBuilder.append(",quLogics:["); // 题目的逻辑
		List<QuestionLogic> questionLogics=entity.getQuestionLogics();
		if(questionLogics!=null){
			for (QuestionLogic questionLogic : questionLogics) {
				stringBuilder.append("{id:'").append(questionLogic.getId());
				stringBuilder.append("',title:'").append(questionLogic.getTitle()).append("'},");
			}
		}
		// 处理多余的逗号
		int strLen=stringBuilder.length();
		if(stringBuilder.lastIndexOf(",")==(strLen-1)){
			stringBuilder.replace(strLen-1, strLen, "");
		}
		stringBuilder.append("]}");
		return stringBuilder.toString();
	}

}
