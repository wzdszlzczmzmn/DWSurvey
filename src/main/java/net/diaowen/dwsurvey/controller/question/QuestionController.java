package net.diaowen.dwsurvey.controller.question;

import net.diaowen.dwsurvey.service.QuestionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
	private final QuestionManager questionManager;

	@Autowired
	public QuestionController(QuestionManager questionManager) {
		this.questionManager = questionManager;
	}

	/**
	 * ajax删除
	 * @return 删除成则向客户端返回 true，出现异常则返回 false
	 */
	@RequestMapping("/ajaxDelete.do")
	public String ajaxDelete(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String responseStr="";
		try{
			// 从请求中获取问题 id
			String delQuId=request.getParameter("quId");
			questionManager.delete(delQuId);
			responseStr="true";
		}catch (RuntimeException e) {
			responseStr="false";
		}
		response.getWriter().write(responseStr);
		return null;
	}

}
