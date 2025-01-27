package com.example.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.annotation.SysLogger;
import com.example.entity.Place;
import com.example.entity.RestBean;
import com.example.entity.dto.PlaceDTO;
import com.example.entity.vo.PlaceVO;
import com.example.service.PlaceService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/place")
public class PlaceController {
    
    @Resource
    private PlaceService service;

    @GetMapping("/")
    @SysLogger("获取场地列表")
    public RestBean<List<PlaceVO>> listPlaces() {
        return RestBean.success(service.listAllPlaces());
    }

    @GetMapping("/{id}")
    @SysLogger("获取场地详情")
    public RestBean<PlaceVO> getPlace(@PathVariable Integer id) {
        PlaceVO place = service.getPlaceById(id);
        return place != null ? 
            RestBean.success(place) : 
            RestBean.failure(404, "场地不存在");
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('admin')")
    @SysLogger("创建场地")
    public RestBean<Void> createPlace(@RequestBody @Valid PlaceDTO dto) {
        return service.createPlace(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "创建失败");
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('admin')")
    @SysLogger("更新场地状态")
    public RestBean<Void> updatePlace(@RequestBody @Valid PlaceDTO dto) {
        return service.updatePlace(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "更新失败");
    }

    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('admin')")  
    // @SysLogger("删除场地")
    // public RestBean<Void> deletePlace(@PathVariable Integer id) {
    //     return service.deletePlace(id) ?
    //         RestBean.success() :
    //         RestBean.failure(400, "删除失败");
    // }
}
