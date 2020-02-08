package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;
import com.ev.custom.domain.ProductDO;
import com.ev.custom.domain.ProductTypeDO;
import com.ev.custom.service.ProductService;
import com.ev.custom.service.ProductTypeService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author gumingjie
 * @date 2019/9/09
 */
@ApiIgnore
@Api(value = "/",tags = "产品管理API")
@RestController
public class ProductApiController {
    @Autowired
	private ProductService productService;
    @Autowired
  	private ProductTypeService productTypeService;
      
    @EvApiByToken(value = "/apis/product/addType",method = RequestMethod.POST,apiTitle = "添加产品类型")
    @ApiOperation("添加产品类型")
    public R addProductType(ProductTypeDO productTypeDO){
    	 Map<String, Object> result = productTypeService.add(productTypeDO);
        if (result.containsKey("error")) {
        	return R.error(result.get("error").toString());
		}
        return	R.ok(result) ;
    }
    
    @EvApiByToken(value = "/apis/product/typeList",method = RequestMethod.POST,apiTitle = "产品类型列表")
    @ApiOperation("产品类型列表")
    public R typeList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
            	@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
            	@ApiParam(value = "父节点ID",required = false) @RequestParam(value = "parentId",defaultValue = "0",required = false) Long parentId
            	){
    	 Map<String,Object> results = Maps.newHashMap();
    	 Map<String, Object>params=Maps.newHashMap();
    	 params.put("pagesize", pagesize);
    	 params.put("pageno", pageno);
    	 params.put("parentId", parentId);
         List<Map<String,Object>> data = productTypeService.listForMap(params);
         int total = productTypeService.countForMap(params);
         if(data!=null && data.size()>0){
             DsResultResponse dsRet = new DsResultResponse();
             dsRet.setDatas(data);
             dsRet.setPageno(pageno);
             dsRet.setPagesize(pagesize);
             dsRet.setTotalRows(total);
             dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
             results.put("data",dsRet);
         }
         return  R.ok(results);
    }
    
    
    @EvApiByToken(value = "/apis/product/editType",method = RequestMethod.POST,apiTitle = "修改产品类型")
    @ApiOperation("修改产品类型")
    public R editType(ProductTypeDO productTypeDO){
    	int code=productTypeService.update(productTypeDO);
    	if(code>0){
            return R.ok();
        }else if(code == -1){
       	  return R.error(code,"类型名已存在");
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/product/deleteType",method = RequestMethod.POST,apiTitle = "删除产品类型")
    @ApiOperation("删除产品类型")
    public R deleteType(@ApiParam(value = "产品类型ID",required = true) @RequestParam(value = "productTypeId",defaultValue = "",required = true)Long productTypeId){
        if(productTypeService.remove(productTypeId)>0){
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/product/add",method = RequestMethod.POST,apiTitle = "添加产品")
    @ApiOperation("添加产品")
    public R addProduct(ProductDO productDO){
    	 Map<String, Object> result = productService.add(productDO);
    	 if (result.containsKey("error")) {
         	return R.error(result.get("error").toString());
 		}
         return	R.ok(result) ;
    }
    
    @EvApiByToken(value = "/apis/product/inList",method = RequestMethod.POST,apiTitle = "获取入库时产品列表")
    @ApiOperation("获取入库时产品列表")
    public R inList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
            	@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
            	@ApiParam(value = "产品类型",required = false) @RequestParam(value = "productTypeId",defaultValue = "",required = false) Long productTypeId,
            	@ApiParam(value = "产品编号或名称或型号查询",required = false) @RequestParam(value = "fuzzySearch",defaultValue = "",required = false) String fuzzySearch,
            	@ApiParam(value = "仓库类型",required = false) @RequestParam(value = "facilityTypeId",defaultValue = "",required = false) Long facilityTypeId,
            	@ApiParam(value = "常用产品选择(0:全部显示 1:常用)",required = false) @RequestParam(value = "usage",defaultValue = "",required = false) Integer usage
            	){
    	 Map<String,Object> results = Maps.newHashMap();
    	 Map<String, Object>params=Maps.newHashMap();
    	 params.put("pagesize", pagesize);
    	 params.put("pageno", pageno);
    	 params.put("productTypeId", productTypeId);
    	 params.put("fuzzySearch", fuzzySearch);
    	 params.put("facilityTypeId", facilityTypeId);
    	 params.put("usage", usage);
         List<Map<String,Object>> data = productService.listForMap(params);
         int total = productService.countForMap(params);
         if(data!=null && data.size()>0){
             DsResultResponse dsRet = new DsResultResponse();
             dsRet.setDatas(data);
             dsRet.setPageno(pageno);
             dsRet.setPagesize(pagesize);
             dsRet.setTotalRows(total);
             dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
             results.put("data",dsRet);
         }
         return  R.ok(results);
    }
    @EvApiByToken(value = "/apis/product/outList",method = RequestMethod.POST,apiTitle = "获取出库时产品列表")
    @ApiOperation("获取出库时产品列表")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
            	@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
            	@ApiParam(value = "产品类型",required = false) @RequestParam(value = "productTypeId",defaultValue = "",required = false) Long productTypeId,
            	@ApiParam(value = "产品编号或名称或型号查询",required = false) @RequestParam(value = "fuzzySearch",defaultValue = "",required = false) String fuzzySearch,
            	@ApiParam(value = "仓库类型",required = false) @RequestParam(value = "facilityTypeId",defaultValue = "",required = false) Long facilityTypeId
            	){
    	 Map<String,Object> results = Maps.newHashMap();
    	 Map<String, Object>params=Maps.newHashMap();
    	 params.put("pagesize", pagesize);
    	 params.put("pageno", pageno);
    	 params.put("productTypeId", productTypeId);
    	 params.put("fuzzySearch", fuzzySearch);
    	 params.put("facilityTypeId", facilityTypeId);
         List<Map<String,Object>> data = productService.stockListForMap(params);
         int total = productService.stockCountForMap(params);
         if(data!=null && data.size()>0){
             DsResultResponse dsRet = new DsResultResponse();
             dsRet.setDatas(data);
             dsRet.setPageno(pageno);
             dsRet.setPagesize(pagesize);
             dsRet.setTotalRows(total);
             dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
             results.put("data",dsRet);
         }
         return  R.ok(results);
    }
    
    @EvApiByToken(value = "/apis/product/editUsage",method = RequestMethod.POST,apiTitle = "设置产品常用度")
    @ApiOperation("设置产品常用度")
    public R editUsage(@ApiParam(value = "产品ID",required = true) @RequestParam(value = "id",defaultValue = "",required = true) Long id,
        			  @ApiParam(value = "产品常用度(0:初始状态 1:常用)",required = true) @RequestParam(value = "usage",defaultValue = "",required = true) Integer usage){
    	 ProductDO productDO=new ProductDO();
    	 productDO.setId(id);
    	 productDO.setUsage(usage);
    	if(productService.update(productDO)>0){
            return R.ok();
        }
        return R.ok("操作成功！");
    }
    
    @EvApiByToken(value = "/apis/product/edit",method = RequestMethod.POST,apiTitle = "修改产品")
    @ApiOperation("修改产品")
    public R edit(ProductDO productDO){
    
    	int code = productService.update(productDO);
    	if(code>0){
            return R.ok();
        }else if(code == -1){
       	  return R.error(code,"编号存在");
        }
        return R.ok("操作成功！");
    }

    @EvApiByToken(value = "/apis/product/delete",method = RequestMethod.POST,apiTitle = "删除产品")
    @ApiOperation("删除产品")
    public R delete(@ApiParam(value = "产品ID",required = true) @RequestParam(value = "productId",defaultValue = "",required = true)Long productId){
        if (productService.checkDelete(productId)>0) {
        	   return R.error("该产品已被使用不能被删除");
		}
    	if(productService.remove(productId)>0){
            return R.ok();
        }
        return R.error();
    }
}
