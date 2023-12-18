package net.diaowen.dwsurvey.controller.question;

import net.diaowen.common.CheckType;
import net.diaowen.common.QuType;
import net.diaowen.dwsurvey.entity.QuCheckbox;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.entity.QuestionLogic;
import net.diaowen.dwsurvey.service.QuCheckboxManager;
import net.diaowen.dwsurvey.service.QuestionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 多选题 action
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
@Controller
@RequestMapping("/api/dwsurvey/app/design/qu-checkbox")
public class QuCheckboxController {
	@Autowired
	private QuestionManager questionManager;
	@Autowired
	private QuCheckboxManager quCheckboxManager;

	/**
	 * 处理 保存多选题 的请求，并返回一个包含结果的JSON字符串
	 *
	 * @param request
	 * @param response
	 * @return 返回一个包含结果的JSON字符串
	 * @throws Exception
	 */
	@RequestMapping("/ajaxSave.do")
	public String ajaxSave(HttpServletRequest request,HttpServletResponse response) throws IOException {
		try{
			// 从请求中构建并保存多选题
			Question entity=ajaxBuildSaveOption(request);
			questionManager.save(entity);
			// 构建结果JSON并返回
			String resultJson=buildResultJson(entity);
			response.getWriter().write(resultJson);
		}catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("error");
		}
		return null;
	}

	/**
	 * 处理 保存多选题 的具体逻辑
	 * 从请求中获取问题和选项的属性，设置这些属性的值
	 * 最后将问题和选项保存到数据库中
	 *
	 * @param request
	 * @return 返回保存后的多选题对象
	 * @throws UnsupportedEncodingException
	 */
	private Question ajaxBuildSaveOption(HttpServletRequest request) throws UnsupportedEncodingException {
		// 从请求中获取问题和选项的属性
		String quId=request.getParameter("quId");
		String belongId=request.getParameter("belongId");
		String quTitle=request.getParameter("quTitle");
		String orderById=request.getParameter("orderById");
		String tag=request.getParameter("tag");
		String isRequired=request.getParameter("isRequired");
		String hv=request.getParameter("hv");
		String randOrder=request.getParameter("randOrder");
		String cellCount=request.getParameter("cellCount");
		String contactsAttr=request.getParameter("contactsAttr");
		String contactsField=request.getParameter("contactsField");
		String paramInt01=request.getParameter("paramInt01");//最小分
		String paramInt02=request.getParameter("paramInt02");//最大分

		// 对空字符串进行处理
		if("".equals(quId)){
			quId=null;
		}
		// 创建 Question 对象并设置属性
		Question entity=questionManager.getModel(quId);
		entity.setBelongId(belongId);
		if(quTitle!=null){
			quTitle=URLDecoder.decode(quTitle,"utf-8");
			entity.setQuTitle(quTitle);
		}
		entity.setOrderById(Integer.parseInt(orderById));
		entity.setTag(Integer.parseInt(tag));
		entity.setQuType(QuType.CHECKBOX);

		// 参数
		isRequired=(isRequired==null || "".equals(isRequired))?"0":isRequired;
		hv=(hv==null || "".equals(hv))?"0":hv;
		randOrder=(randOrder==null || "".equals(randOrder))?"0":randOrder;
		cellCount=(cellCount==null || "".equals(cellCount))?"0":cellCount;
		contactsAttr=(contactsAttr==null || "".equals(contactsAttr))?"0":contactsAttr;
		paramInt01=(paramInt01==null || "".equals(paramInt01))?"0":paramInt01;
		paramInt02=(paramInt02==null || "".equals(paramInt02))?"0":paramInt02;
		entity.setContactsAttr(Integer.parseInt(contactsAttr));
		entity.setContactsField(contactsField);
		entity.setIsRequired(Integer.parseInt(isRequired));
		entity.setHv(Integer.parseInt(hv));
		entity.setRandOrder(Integer.parseInt(randOrder));
		entity.setCellCount(Integer.parseInt(cellCount));
		entity.setParamInt01(Integer.parseInt(paramInt01));
		entity.setParamInt02(Integer.parseInt(paramInt02));

		// 获取并处理选项
		Map<String, Object> optionNameMap=WebUtils.getParametersStartingWith(request, "optionValue_");
		List<QuCheckbox> quCheckboxs=new ArrayList<>();
		for (Map.Entry<String,Object> entry: optionNameMap.entrySet()) {
			String key = entry.getKey();
			String optionId=request.getParameter("optionId_"+key);
			String isNote=request.getParameter("isNote_"+key);
			String checkType=request.getParameter("checkType_"+key);
			String isRequiredFill=request.getParameter("isRequiredFill_"+key);

			Object optionName=optionNameMap.get(key);
			String optionNameValue=(optionName!=null)?optionName.toString():"";
			QuCheckbox quCheckbox=new QuCheckbox();
			if("".equals(optionId)){
				optionId=null;
			}
			quCheckbox.setId(optionId);
			optionNameValue=URLDecoder.decode(optionNameValue,"utf-8");
			quCheckbox.setOptionName(optionNameValue);
			quCheckbox.setOrderById(Integer.parseInt(key));
			isNote=(isNote==null || "".equals(isNote))?"0":isNote;
			checkType=(checkType==null || "".equals(checkType))?"NO":checkType;
			isRequiredFill=(isRequiredFill==null || "".equals(isRequiredFill))?"0":isRequiredFill;
			quCheckbox.setIsNote(Integer.parseInt(isNote));
			quCheckbox.setCheckType(CheckType.valueOf(checkType));
			quCheckbox.setIsRequiredFill(Integer.parseInt(isRequiredFill));
			quCheckboxs.add(quCheckbox);
		}
		entity.setQuCheckboxs(quCheckboxs);

		//逻辑选项设置
		Map<String, Object> quLogicIdMap=WebUtils.getParametersStartingWith(request, "quLogicId_");
		List<QuestionLogic> quLogics=new ArrayList<>();
		for (Map.Entry<String,Object> entry : quLogicIdMap.entrySet()) {
			String key = entry.getKey();
			String cgQuItemId=request.getParameter("cgQuItemId_"+key);
			String skQuId=request.getParameter("skQuId_"+key);
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

	/**
	 * 构建包含问题和选项信息的JSON字符串
	 *
	 * @param entity 问题对象
	 * @return 构建好的JSON字符串
	 */
	public static String buildResultJson(Question entity){
		//{id:'null',quItems:[{id:'null',title:'null'},{id:'null',title:'null'}]}
		StringBuilder strBuf=new StringBuilder();

		//将问题对象的id和orderById属性添加到strBuf中
		strBuf.append("{id:'").append(entity.getId());
		strBuf.append("',orderById:");
		strBuf.append(entity.getOrderById());

		//将问题对象的 选项列表 添加到strBuf中
		strBuf.append(",quItems:[");
		List<QuCheckbox> quCheckboxs=entity.getQuCheckboxs();
		for (QuCheckbox quCheckbox : quCheckboxs) {
			strBuf.append("{id:'").append(quCheckbox.getId());
			strBuf.append("',title:'").append(quCheckbox.getOrderById()).append("'},");
		}
		int strLen=strBuf.length();
		if(strBuf.lastIndexOf(",")==(strLen-1)){
			strBuf.replace(strLen-1, strLen, "");
		}
		strBuf.append("]");

		//添加quLogics（逻辑列表）属性
		strBuf.append(",quLogics:[");
		List<QuestionLogic> questionLogics=entity.getQuestionLogics();
		if(questionLogics!=null){
			for (QuestionLogic questionLogic : questionLogics) {
				strBuf.append("{id:'").append(questionLogic.getId());
				strBuf.append("',title:'").append(questionLogic.getTitle()).append("'},");
			}
		}
		strLen=strBuf.length();
		if(strBuf.lastIndexOf(",")==(strLen-1)){
			strBuf.replace(strLen-1, strLen, "");
		}
		strBuf.append("]}");
		return strBuf.toString();
	}

	/**
	 * 删除选项
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ajaxDelete.do")
	public String ajaxDelete(HttpServletRequest request,HttpServletResponse response) throws IOException {
		try{
			String quItemId=request.getParameter("quItemId");
			quCheckboxManager.ajaxDelete(quItemId);
			response.getWriter().write("true");
		}catch(Exception e){
			e.printStackTrace();
			response.getWriter().write("error");
		}
		return null;
	}


}
