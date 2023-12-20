package net.diaowen.common.base.controller;

import com.octo.captcha.service.image.ImageCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import net.diaowen.common.exception.JcaptchaException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 处理生成和渲染 CAPTCHA 图像请求的控制器。
 */
@Controller
//@RequestMapping("/jcap")
@RequestMapping("/api/dwsurvey/anon/jcap")
public class JcaptchaController {

	@Autowired
    private ImageCaptchaService imageCaptchaService;
    /**
     * Generates a new CAPTCHA image and renders it as a JPEG image.
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @return null
     * @throws IOException if an I/O error occurs
     */
    @RequestMapping("/jcaptcha.do")
	public String execute(HttpServletRequest request,HttpServletResponse response)throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // get the session id that will identify the generated captcha.
            // the same id must be used to validate the response, the session id
            // is a good candidate!
            String captchaId = request.getSession().getId();
            // call the ImageCaptchaService getChallenge method
            BufferedImage challenge = imageCaptchaService.getImageChallengeForID(captchaId, request.getLocale());
            // a jpeg encoder
            jpegOutputStream = new ByteArrayOutputStream();
            ImageIO.write(challenge,"jpg",jpegOutputStream);
        } catch (Exception e) {
            throw new JcaptchaException("验证码错误");
        }
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        // flush it in the response
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
        return null;
	}

}
