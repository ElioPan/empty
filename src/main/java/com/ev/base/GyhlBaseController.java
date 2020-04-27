package com.ev.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ev.framework.utils.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Author xy
 * @Date 2020/4/26 15:00
 * @Description
 */
public abstract class GyhlBaseController<T extends Serializable> {
    @Autowired
    public abstract IService<T> getService();

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "通过id获取信息")
    public T getById(@PathVariable Object id){

        return (T) getService().getById((Serializable)id);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取全部数据")
    public Response<List<T>> getAll(){

        List<T> list = getService().list(null);
        return Response.succeed(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Response<IPage<T>> getByPage(@ModelAttribute Page<T> page){
        IPage<T> data = getService().page(page,null);
        return Response.succeed(data);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存数据")
    public Response<Object> save(@ModelAttribute T entity){
        getService().save(entity);
        return Response.succeed(entity,"保存成功");
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "更新数据")
    public Response<Object> update(@ModelAttribute T entity){

        getService().updateById(entity);
        return Response.succeed(entity,"更新成功");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation(value = "批量通过id删除")
    public Response<Object> delAllByIds(@PathVariable Collection<? extends Serializable> ids){

        getService().removeByIds(ids);
        return Response.succeed("批量通过id删除数据成功");
    }
}
