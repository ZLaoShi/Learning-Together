package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.StudyRequest;
import com.example.entity.dto.StudyRequestDTO;
import com.example.entity.vo.StudyRequestVO;
import java.util.List;

public interface StudyRequestService extends IService<StudyRequest> {
    List<StudyRequestVO> getUserRequests();
    List<StudyRequestVO> getPendingRequests(); 
    boolean createRequest(StudyRequestDTO dto);
    boolean acceptRequest(Integer id);
    boolean rejectRequest(Integer id);
}