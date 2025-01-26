package com.example.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Place;
import com.example.entity.RestBean;
import com.example.entity.dto.PlaceDTO;
import com.example.service.PlaceService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/place")
public class PlaceController {
    
    @Resource
    private PlaceService service;

    @GetMapping("/")
    public RestBean<List<Place>> listPlaces() {
        return RestBean.success(service.list());
    }

    @GetMapping("/{id}")
    public RestBean<Place> getPlace(@PathVariable Integer id) {
        return RestBean.success(service.getById(id));
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Void> createPlace(@RequestBody @Valid PlaceDTO dto) {
        return service.createPlace(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "创建失败");
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Void> updatePlace(@RequestBody @Valid PlaceDTO dto) {
        return service.updatePlace(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "更新失败");
    }
}
