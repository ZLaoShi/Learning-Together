package com.example.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.MatchRecord;
import com.example.entity.vo.MatchVO;
import com.example.entity.vo.TimeSlot;

public interface MatchService extends IService<MatchRecord> {
    // 结对匹配
    List<MatchVO> matchBySubject();
    // 按时间匹配
    List<MatchVO> matchByTime();
    // 按地点匹配
    List<MatchVO> matchByPlace();
    // 互补匹配
    List<MatchVO> matchByComplement();

    List<MatchVO> getUserMatches();
    List<MatchVO> getPendingMatches();
    boolean confirmMatch(Integer id);
    boolean rejectMatch(Integer id);
    boolean completeMatch(Integer id);
}
