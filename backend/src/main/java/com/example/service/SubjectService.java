package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Subject;
import com.example.entity.dto.SubjectDTO;
import com.example.entity.vo.SubjectVO;
import java.util.List;

public interface SubjectService extends IService<Subject> {
    List<SubjectVO> listAllSubjects();
    SubjectVO getSubjectById(Integer id);
    boolean createSubject(SubjectDTO dto);
    boolean updateSubject(SubjectDTO dto);
}