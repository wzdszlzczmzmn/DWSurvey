package net.diaowen.dwsurvey.controller.question;

import net.diaowen.dwsurvey.service.QuestionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 题目 action
 * @author KeYuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
@Controller
@RequestMapping("/api/dwsurvey/app/design/question")
public class QuestionController{
	@Autowired
	private QuestionManager questionManager;
	/**
	 * ajax删除
	 * @return true if delete successfully, or false if catch en exception
	 * @throws Exception
	 */
	@RequestMapping("/ajaxDelete.do")
	public String ajaxDelete(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String responseStr="";
		try{
			// 从请求中获取问题 id
			String delQuId=request.getParameter("quId");
			questionManager.delete(delQuId);
			responseStr="true";
		}catch (Exception e) {
			responseStr="false";
		}
		response.getWriter().write(responseStr);
		return null;
	}

}
