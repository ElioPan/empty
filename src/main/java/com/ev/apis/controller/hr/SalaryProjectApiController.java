package com.ev.apis.controller.hr;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.hr.domain.SalaryProjectDO;
import com.ev.hr.service.SalaryProjectService;
import com.ev.hr.vo.SalaryProjectPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 薪资项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 13:10:37
 */

@Api(value = "/",tags = "薪资项目API")
@RestController
public class SalaryProjectApiController {

	/**
	 * 薪资项目 服务类
	 */
	@Autowired
	private SalaryProjectService salaryProjectService;

	/**
	 * 方法描述: [根据条件分页查询薪资项目.]</br>
	 * 初始作者: 顾明杰<br/>
	 * 创建日期: 2019年11月17日-下午3:20:53<br/>
	 * 开始版本: 1.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param pageParam 分页参数
	 * @return R 响应对象
	 */
	@EvApiByToken(value = "/apis/salary/project/pageList", method = RequestMethod.POST, apiTitle = "根据条件分页查询薪资项目")
	@ApiOperation("根据条件分页查询薪资项目")
	public R pageList(@Valid @RequestBody @ApiParam("薪资项目分页列表参数对象") SalaryProjectPageParam pageParam) {
		return R.ok(this.salaryProjectService.pageList(pageParam));
	}

	/**
	 * 方法描述: [根据id获取薪资项目.]</br>
	 * 初始作者: 顾明杰<br/>
	 * 创建日期: 2019年11月17日-下午3:20:53<br/>
	 * 开始版本: 1.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param id 项目ID
	 * @return R 响应对象
	 */
	@EvApiByToken(value = "/apis/salary/project/detail/{id}", method = RequestMethod.GET, apiTitle = "根据id获取薪资项目")
	@ApiOperation("根据id获取薪资项目")
	public R detail(@PathVariable("id") @ApiParam("id") Long id) {
		return R.ok(this.salaryProjectService.getById(id));
	}

	/**
	 * 方法描述: [批量删除]</br>
	 * 初始作者: 顾明杰<br/>
	 * 创建日期: 2020/1/2 16:37<br/>
	 * 开始版本: 1.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param ids 项目ID组
	 * @return void
	 */
	@EvApiByToken(value = "/apis/salary/project/removeByIds", method = RequestMethod.POST, apiTitle = "批量删除")
	@ApiOperation("批量删除")
	@Transactional(rollbackFor = Exception.class)
	public R removeByIds(@RequestBody @ApiParam("批量删除") List<Long> ids){
		return this.salaryProjectService.delete(ids);
	}

	/**
	 * 方法描述: [保存薪资项目.]</br>
	 * 初始作者: 顾明杰<br/>
	 * 创建日期: 2019年9月25日-下午4:40:28<br/>
	 * 开始版本: 1.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param saveParam 薪资项目参数
	 * @return R 响应对象
	 */
	@EvApiByToken(value = "/apis/salary/project/save", method = RequestMethod.POST, apiTitle = "保存薪资项目")
	@ApiOperation("保存薪资项目")
	public R save(@Valid @RequestBody @ApiParam("保存个人调薪记录参数对象") SalaryProjectDO saveParam){
		return this.salaryProjectService.saveAndVerify(saveParam);
	}

	/**
	 * 方法描述: [根据条件分页查询薪资项目.]</br>
	 * 初始作者: 顾明杰<br/>
	 * 创建日期: 2019年11月17日-下午3:20:53<br/>
	 * 开始版本: 1.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param type 参数
	 * @return R 响应对象
	 */
	@EvApiByToken(value = "/apis/salary/project/getListByType", method = RequestMethod.POST, apiTitle = "根据条件获取薪资项目下拉框")
	@ApiOperation("根据条件获取薪资项目下拉框")
	public R getListByType(@RequestParam("type") @ApiParam("类型")  String type) {
		return R.ok(this.salaryProjectService.getListByType(type));
	}

}
