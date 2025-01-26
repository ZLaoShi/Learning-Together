package com.example.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Account;
import com.example.entity.MatchRecord;
import com.example.entity.Place;
import com.example.entity.dto.PlaceDTO;
import com.example.entity.vo.MatchVO;
import com.example.entity.vo.PlaceVO;
import com.example.mapper.PlaceMapper;
import com.example.service.PlaceService;

@Service
public class PlaceServiceImpl extends ServiceImpl<PlaceMapper, Place> implements PlaceService {

    @Override
    public List<PlaceVO> listAllPlaces() {
        return this.list().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public PlaceVO getPlaceById(Integer id) {
        Place place = this.getById(id);
        return place != null ? convertToVO(place) : null;
    }

    @Override
    public boolean createPlace(PlaceDTO dto) {
        Place place = new Place();
        BeanUtils.copyProperties(dto, place);
        place.setStatus(1);
        place.setCreateTime(new Date());
        place.setUpdateTime(new Date());
        return this.save(place);
    }

    @Override
    public boolean updatePlace(PlaceDTO dto) {
        Place place = this.getById(dto.getId());
        if(place == null) return false;
        
        BeanUtils.copyProperties(dto, place);
        place.setUpdateTime(new Date());
        return this.updateById(place);
    }

    private PlaceVO convertToVO(Place place) {
        PlaceVO vo = new PlaceVO();
        BeanUtils.copyProperties(place, vo);
        return vo;
    }

}