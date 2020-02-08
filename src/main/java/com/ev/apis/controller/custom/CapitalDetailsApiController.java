package com.ev.apis.controller.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;
import com.ev.custom.dao.PaymentformDao;
import com.ev.custom.dao.ReceiptDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 资金明细控制器层
 * @author hepeng
 *
 */
@RestController
@Api(value = "/",tags = "资金明细API")
public class CapitalDetailsApiController {

	@Autowired
	private ReceiptDao receiptDao;
	
	@Autowired
	private PaymentformDao paymentformDao;
	
	@EvApiByToken(value = "/apis/CapitalDetailsApi/listApi",method = RequestMethod.GET,apiTitle = "获取资金明细")
    @ApiOperation("获取资金明细")
	public R listApi( @ApiParam(value = "开始时间",required = false) @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间",required = false) @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "银行账号",required = false) @RequestParam(value = "payAccount",required = false) String payAccount,
			@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("payAccount",payAccount);
		map.put("pageno",pageno);
		map.put("pagesize",pagesize);
		List<Map<String, Object>> receiptdata = receiptDao.listApi(map);
		List<Map<String, Object>> paydata = paymentformDao.listApi(map);
		List<List<Map<String, Object>>> list = new ArrayList<>();
		list.add(receiptdata);
		list.add(paydata);
		Map<String, Object> result = new HashMap<String,Object>();
		if((receiptdata!=null && receiptdata.size() > 0) || (paydata!=null && paydata.size() > 0)){
			DsResultResponse dsRet = new DsResultResponse();
			int total = receiptDao.countApi(map) + paymentformDao.countApi(map);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total + pagesize - 1) / pagesize);
			result.put("data", dsRet);
			result.put("datas",list);
			return R.ok(result);
        }
		return R.error("无此信息！！");
	}
}
