package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Subject;
import com.example.entity.dto.SubjectDTO;
import com.example.entity.vo.SubjectVO;
import com.example.mapper.SubjectMapper;
import com.example.service.SubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public List<SubjectVO> listAllSubjects() {
        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
                
        // 管理员可以看到所有科目,普通用户只能看到启用的
        if("ROLE_admin".equals(role)) {
            return this.list().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());
        } else {
            return this.lambdaQuery()
                    .eq(Subject::getStatus, 1)  // 只查询启用状态
                    .list()
                    .stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());
        }
    } //TO DO 待其他操作补全逻辑删除相关判断逻辑

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

    @Override
    public boolean deleteSubject(Integer id) {
        Subject subject = this.getById(id);
        if(subject == null) return false;
        
        subject.setStatus(0);  // 0表示禁用
        subject.setUpdateTime(new Date());
        return this.updateById(subject);
    }

    private SubjectVO convertToVO(Subject subject) {
        SubjectVO vo = new SubjectVO();
        BeanUtils.copyProperties(subject, vo);
        return vo;
    }
}