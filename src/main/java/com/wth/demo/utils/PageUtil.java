package com.wth.demo.utils;

import com.github.pagehelper.Page;
import com.wth.demo.vo.resp.PageVo;

import java.util.List;

public class PageUtil {

    private PageUtil(){}

    public static <T> PageVo<T> getPageVo(List<T> list){
        PageVo<T> pageVo = new PageVo<T>();
        if (list instanceof Page){
            Page<T> page = (Page<T>) list;
            pageVo.setTotalRows(page.getTotal());
            pageVo.setCurPageSize(page.getPageSize());
            pageVo.setPageSize(page.size());
            pageVo.setPageNum(page.getPageNum());
            pageVo.setTotalPages(page.getPages());
            pageVo.setList(page.getResult());
        }

        return pageVo;
    }





}
