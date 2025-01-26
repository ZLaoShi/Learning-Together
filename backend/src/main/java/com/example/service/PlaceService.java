package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Place;
import com.example.entity.dto.PlaceDTO;
import com.example.entity.vo.PlaceVO;
import java.util.List;

public interface PlaceService extends IService<Place> {
    List<PlaceVO> listAllPlaces();
    PlaceVO getPlaceById(Integer id);
    boolean createPlace(PlaceDTO dto);
    boolean updatePlace(PlaceDTO dto);
}