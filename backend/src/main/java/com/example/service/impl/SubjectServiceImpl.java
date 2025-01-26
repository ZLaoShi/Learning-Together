package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Subject;
import com.example.entity.dto.SubjectDTO;
import com.example.entity.vo.SubjectVO;
import com.example.mapper.SubjectMapper;
import com.example.service.SubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public List<SubjectVO> listAllSubjects() {
        return this.list().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectVO getSubjectById(Integer id) {
        Subject subject = this.getById(id);
        return subject != null ? convertToVO(subject) : null;
    }

    @Override
    public boolean createSubject(SubjectDTO dto) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(dto, subject);
        subject.setStatus(1);
        subject.setCreateTime(new Date());
        subject.setUpdateTime(new Date());
        return this.save(subject);
    }

    @Override
    public boolean updateSubject(SubjectDTO dto) {
        Subject subject = this.getById(dto.getId());
        if(subject == null) return false;
        
        BeanUtils.copyProperties(dto, subject);
        subject.setUpdateTime(new Date());
        return this.updateById(subject);
    }

    private SubjectVO convertToVO(Subject subject) {
        SubjectVO vo = new SubjectVO();
        BeanUtils.copyProperties(subject, vo);
        return vo;
    }
}