package com.huashan.core.app.user;

import com.huashan.core.base.BaseAction;
import com.huashan.core.base.Service;
import com.huashan.core.beans.MapResponse;
import com.huashan.core.beans.NPC;
import com.huashan.core.beans.User;
import com.huashan.core.beans.WechatUser;
import com.huashan.core.beans.job.UserSeclectAttribute;
import com.huashan.core.webservice.UserService;
import com.huashan.core.webservice.WechatUserService;
import com.huashan.utils.CollectionsUtil;
import com.huashan.utils.ObjectUtil;
import com.huashan.utils.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author lixu
 * 
 * Tue Aug 16 17:25:54 CST 2016
 */

@Controller
@RequestMapping("/user")
public class UserAction extends BaseAction {
	
	@Autowired
	UserService service;

	@Autowired
	WechatUserService wechatUserService;
	
	public Service<User> getService() {
		return service;
	}

	/**
	 * 1.通过微信回调获得参数code
	 * @param code
	 * @param state
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("login")
	public String login(String code, String state, HttpServletRequest request, Model model) {
		if (StringUtils.isNotEmpty(code)) {

//			WechatUser wechatUser = this.wechatUserService.findWechatUsetById(20);
			WechatUser wechatUser = this.wechatUserService.find(20);
			if (wechatUser == null) {
				return "/";
			}
			//2.通过code换取网页授权access_token并存储user信息
			User user = this.service.getAccess_token(wechatUser, code);
			if (user != null && user.getScope().equals("snsapi_userinfo")) {
				user = this.service.getUserInfo(user, wechatUser);
			}else {
				//判断用户是否关注公众号、如果关注了则去获取用户信息
			}
			request.getSession().setAttribute(User.USERSESSIONLOG, user);
			model.addAttribute("user", user);
		}
		return "/";
	}

	@RequestMapping("sendUserSubject")
	@ResponseBody
	public MapResponse<String> sendUserSubject(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		if (CollectionsUtil.isEmpty(map)) {
			return MapResponse.failResponse("数据为空！");
		}
		JSONObject jsonObject = JSONObject.fromObject(map);
		User user = (User)request.getSession().getAttribute(User.USERSESSIONLOG);
		MapResponse<String> mapResponse = this.service.sendUserSubJect(jsonObject.toString(), user);
		return mapResponse;
	}

	@RequestMapping("getUserSubject")
	@ResponseBody
	public MapResponse<UserSeclectAttribute> getUserSubject(@RequestBody Map map) {
		Object obj = map.get("userId");
		if (ObjectUtil.isEmpty(obj)) {
			return MapResponse.failResponse("查询ID为空！");
		}
		Integer userId = obj == null? null:Integer.parseInt(obj.toString());
		MapResponse<UserSeclectAttribute> mapMapResponse = this.service.getUserSebject(userId);
		return mapMapResponse;
	}

	@RequestMapping("getPeakUserInfo")
	@ResponseBody
	public MapResponse<List<NPC>> getPeakUserInfo(HttpServletRequest request){
		List<NPC> list = this.service.getPeakUserInfo();
		return  MapResponse.successResponse(list);
	}

	@RequestMapping("userPk")
	@ResponseBody
	public MapResponse<Integer> userPk(@RequestBody Map map) {
		Object userId = map.get("userId");
		Object code = map.get("code");
		if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(code)) {
			return MapResponse.failResponse("请求数据为空！");
		}
		Integer id = Integer.parseInt(userId.toString());
		Integer co = Integer.parseInt(code.toString());
		boolean flag = this.service.userPk(id, co);
		if (flag) {
			return MapResponse.successResponse(1);
		}
		return MapResponse.successResponse(0);
	}
}
