package com.huashan.core.app.pointinfo;

import com.huashan.core.base.BaseAction;
import com.huashan.core.beans.MapResponse;
import com.huashan.core.beans.PointDetailed;
import com.huashan.core.beans.PointInfo;
import com.huashan.core.webservice.PointInfoService;
import com.huashan.utils.CollectionsUtil;
import com.huashan.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author lixu
 *         <p>
 *         Wed Oct 26 23:07:26 CST 2016
 */

@Controller
@RequestMapping("/pointInfo")
public class PointInfoAction extends BaseAction {

    @Autowired
    PointInfoService service;

    @RequestMapping("getAll")
    @ResponseBody
    public MapResponse<List<PointInfo>> getAll() {
        List<PointInfo> list = this.service.findCascadeAll();
        if (CollectionsUtil.isEmpty(list)) {
            return MapResponse.failResponse("数据为空");
        } else {
            return MapResponse.successResponse(list);
        }
    }

    @RequestMapping("getPointDetailed")
    @ResponseBody
    public MapResponse<PointDetailed> getPointDetailed(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) {
//        Integer id = (Integer)map.get("id");
        Object obj = map.get("id");
        Integer id = obj == null? null:Integer.parseInt(obj.toString());
        PointDetailed vo = null;
        if (id == null) {
            return MapResponse.failResponse("id不能为空！");
        }
        vo = this.service.findPointDetailed(id);
        if (vo == null) {
            return MapResponse.failResponse("数据为空！");
        } else {
            return MapResponse.successResponse(vo);
        }
    }
}
