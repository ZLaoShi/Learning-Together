package com.example.service.impl;

// Spring相关
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

// MyBatis Plus相关
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

// 实体类
import com.example.entity.Account;
import com.example.entity.MatchRecord;
import com.example.entity.Place;
import com.example.entity.Subject;
import com.example.entity.UserProfile;
import com.example.entity.vo.MatchVO;
import com.example.entity.vo.TimeSlot;

// Mapper & Service
import com.example.mapper.MatchMapper;
import com.example.service.AccountService;
import com.example.service.MatchService;
import com.example.service.PlaceService;
import com.example.service.SubjectService;
import com.example.service.UserProfileService;
import com.example.utils.JsonTimeUtil;
// 工具类
import com.example.utils.JwtUtils;
import java.util.function.Function;

// Jakarta
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

// 其他工具
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MatchServiceImpl extends ServiceImpl<MatchMapper, MatchRecord> implements MatchService {

    @Resource
    private UserProfileService userProfileService;
    
    @Resource
    private SubjectService subjectService;
    
    @Resource
    private AccountService accountService;

    @Resource
    private JwtUtils jwtUtils;  

    @Resource
    private HttpServletRequest request;  

    @Resource
private PlaceService placeService;

    // 结对匹配
    @Override
    public List<MatchVO> matchBySubject() {
        List<MatchVO> matches = doMatch("pair", profile -> {
            // 计算匹配分数
            List<MatchVO> possibleMatches = new ArrayList<>();
            List<UserProfile> others = userProfileService.list().stream()
                    .filter(p -> !p.getUserId().equals(profile.getUserId()))
                    .map(p -> userProfileService.getUserProfile(p.getUserId()))
                    .collect(Collectors.toList());

            for(UserProfile other : others) {
                for(Integer subjectId : profile.getGoodSubjects()) {
                    BigDecimal score = calculateSubjectMatchScore(profile, other, subjectId);
                    if(score.compareTo(BigDecimal.ZERO) > 0) {
                        possibleMatches.add(createMatchVO(profile, other, "subject", subjectId, null, score));
                    }
                }
            }
            return possibleMatches;
        });

        // 保存最佳匹配
        if(!matches.isEmpty()) {
            MatchVO bestMatch = matches.get(0);
            MatchRecord record = new MatchRecord();
            record.setUserId1(bestMatch.getUserId1());
            record.setUserId2(bestMatch.getUserId2());
            record.setMatchType("pair");
            record.setSubjectId(bestMatch.getSubjectId());
            record.setMatchScore(bestMatch.getMatchScore());
            record.setStatus(0);
            record.setCreateTime(new Date());
            this.save(record);
        }

        return matches;
    }

    //按时间匹配  
    @Override
    public List<MatchVO> matchByTime() {  
        List<MatchVO> matches = doMatch("time", profile -> {
            List<MatchVO> possibleMatches = new ArrayList<>();
            List<UserProfile> others = userProfileService.list().stream()
                    .filter(p -> !p.getUserId().equals(profile.getUserId()))
                    .map(p -> userProfileService.getUserProfile(p.getUserId()))
                    .collect(Collectors.toList());

            for(UserProfile other : others) {
                BigDecimal score = calculateTimeMatchScore(profile, other);  
                if(score.compareTo(BigDecimal.ZERO) > 0) {
                    possibleMatches.add(createMatchVO(profile, other, "time", null, null, score));
                }
            }
            return possibleMatches;
        });

        // 保存最佳匹配
        if(!matches.isEmpty()) {
            MatchVO bestMatch = matches.get(0);
            MatchRecord record = new MatchRecord();
            record.setUserId1(bestMatch.getUserId1());
            record.setUserId2(bestMatch.getUserId2());
            record.setMatchType("time");
            // Note: time_slot字段预留,当前业务场景下仅需记录匹配类型,无需存储具体时间段
            record.setMatchScore(bestMatch.getMatchScore());
            record.setStatus(0);
            record.setCreateTime(new Date());
            this.save(record);
        }

        return matches;
    }

    //按地点匹配
    @Override
    public List<MatchVO> matchByPlace() {
        List<MatchVO> matches = doMatch("place", profile -> {
            List<MatchVO> possibleMatches = new ArrayList<>();
            List<UserProfile> others = userProfileService.list().stream()
                    .filter(p -> !p.getUserId().equals(profile.getUserId()))
                    .map(p -> userProfileService.getUserProfile(p.getUserId()))
                    .collect(Collectors.toList());

            for(UserProfile other : others) {
                BigDecimal score = calculatePlaceMatchScore(profile, other);
                if(score.compareTo(BigDecimal.ZERO) > 0) {
                    possibleMatches.add(createMatchVO(profile, other, "place", null, null, score));
                }
            }
            return possibleMatches;
        });

        // 保存最佳匹配
        if(!matches.isEmpty()) {
            MatchVO bestMatch = matches.get(0);
            MatchRecord record = new MatchRecord();
            record.setUserId1(bestMatch.getUserId1());
            record.setUserId2(bestMatch.getUserId2());
            record.setMatchType("place");
            record.setMatchScore(bestMatch.getMatchScore());
            record.setStatus(0);
            record.setCreateTime(new Date());
            this.save(record);
        }

        return matches;
    }

    // 互补匹配
    @Override
    public List<MatchVO> matchByComplement() {
        List<MatchVO> matches = doMatch("complement", profile -> {
            List<MatchVO> possibleMatches = new ArrayList<>();
            List<UserProfile> others = userProfileService.list().stream()
                    .filter(p -> !p.getUserId().equals(profile.getUserId()))
                    .map(p -> userProfileService.getUserProfile(p.getUserId()))
                    .collect(Collectors.toList());

            for(UserProfile other : others) {
                BigDecimal score = calculateComplementMatchScore(profile, other);
                if(score.compareTo(BigDecimal.ZERO) > 0) {
                    possibleMatches.add(createMatchVO(profile, other, "complement", null, null, score));
                }
            }
            return possibleMatches;
        });

        // 保存最佳匹配
        if(!matches.isEmpty()) {
            MatchVO bestMatch = matches.get(0);
            MatchRecord record = new MatchRecord();
            record.setUserId1(bestMatch.getUserId1());
            record.setUserId2(bestMatch.getUserId2());
            record.setMatchType("complement");
            record.setMatchScore(bestMatch.getMatchScore());
            record.setStatus(0);
            record.setCreateTime(new Date());
            this.save(record);
        }

        return matches;
    }

    // 结对匹配实现
    private BigDecimal calculateSubjectMatchScore(UserProfile p1, UserProfile p2, Integer subjectId) {
        // 基础分30分
        BigDecimal score = BigDecimal.valueOf(30);
        
        log.info("开始计算用户{}和用户{}的科目{}匹配度", p1.getUserId(), p2.getUserId(), subjectId);
        
        // 防空判断
        if(p1.getGoodSubjects() == null || p2.getGoodSubjects() == null ||
           p1.getNeedSubjects() == null || p2.getNeedSubjects() == null) {
            log.warn("用户科目数据为空: p1Good={}, p1Need={}, p2Good={}, p2Need={}", 
                p1.getGoodSubjects(), p1.getNeedSubjects(), p2.getGoodSubjects(), p2.getNeedSubjects());
            return BigDecimal.ZERO;
        }
        
        // 1. 检查科目重叠（至少一方擅长该科目）
        boolean p1Good = p1.getGoodSubjects().contains(subjectId);
        boolean p2Good = p2.getGoodSubjects().contains(subjectId);
        boolean p1Need = p1.getNeedSubjects().contains(subjectId);
        boolean p2Need = p2.getNeedSubjects().contains(subjectId);
        
        log.info("科目{}: p1擅长={}, p2擅长={}, p1需要={}, p2需要={}", 
            subjectId, p1Good, p2Good, p1Need, p2Need);
        
        // 结对匹配：双方的擅长科目或需要帮助的科目有重叠
        boolean hasOverlap = (p1Good && p2Good) || (p1Need && p2Need) || (p1Good && p2Need) || (p2Good && p1Need);
        if(!hasOverlap) {
            log.info("科目{}没有重叠，得分0", subjectId);
            return BigDecimal.ZERO;
        }
    
        // 2. 时间匹配度(40分)
        if(p1.getAvailableTimes() != null && p2.getAvailableTimes() != null) {
            List<TimeSlot> p1Times = JsonTimeUtil.convertToTimeSlots(p1.getAvailableTimes());
            List<TimeSlot> p2Times = JsonTimeUtil.convertToTimeSlots(p2.getAvailableTimes());
            
            long matchingTimeSlots = p1Times.stream()
                    .filter(t1 -> p2Times.stream()
                            .anyMatch(t2 -> t2.getWeekday().equals(t1.getWeekday()) 
                                    && hasTimeOverlap(t1.getTime(), t2.getTime())))
                    .count();
            BigDecimal timeScore = BigDecimal.valueOf(matchingTimeSlots * 10);
            log.info("时间匹配得分: {}", timeScore);
            score = score.add(timeScore);
        }
        
        // 3. 地点相近(30分)
        if(p1.getPreferredPlaces() != null && p2.getPreferredPlaces() != null) {
            long matchingPlaces = p1.getPreferredPlaces().stream()
                    .filter(p2.getPreferredPlaces()::contains)
                    .count();
            BigDecimal placeScore = BigDecimal.valueOf(matchingPlaces * 10);
            log.info("地点匹配得分: {}", placeScore);
            score = score.add(placeScore);
        }
        
        log.info("最终匹配得分: {}", score);
        return score;
    }
    
    // 时间匹配实现
    private BigDecimal calculateTimeMatchScore(UserProfile p1, UserProfile p2) {
        // 基础分30分
        BigDecimal score = BigDecimal.valueOf(30);
        
        // 防空判断
        if(p1.getAvailableTimes() == null || p2.getAvailableTimes() == null) {
            return BigDecimal.ZERO;
        }
        
        // 使用JsonTimeUtil转换
        List<TimeSlot> p1Times = JsonTimeUtil.convertToTimeSlots(p1.getAvailableTimes());
        List<TimeSlot> p2Times = JsonTimeUtil.convertToTimeSlots(p2.getAvailableTimes());
        
        // 检查时间段重合度
        long matchingSlots = p1Times.stream()
                .filter(t1 -> p2Times.stream()
                        .anyMatch(t2 -> t2.getWeekday().equals(t1.getWeekday()) 
                                && hasTimeOverlap(t1.getTime(), t2.getTime())))
                .count();
                
        score = score.add(BigDecimal.valueOf(matchingSlots * 10));
        return score;
    }
    
    // 地点匹配实现
    private BigDecimal calculatePlaceMatchScore(UserProfile p1, UserProfile p2) {
        // 基础分20分
        BigDecimal score = BigDecimal.valueOf(20);
        
        // 防空判断
        if(p1.getPreferredPlaces() == null || p2.getPreferredPlaces() == null) {
            return BigDecimal.ZERO;
        }
        
        // 检查地点重合度
        List<Integer> p1Places = p1.getPreferredPlaces();
        List<Integer> p2Places = p2.getPreferredPlaces();
        
        // 计算共同偏好地点数量
        long matchingPlaces = p1Places.stream()
                .filter(p2Places::contains)
                .count();
                
        // 每个共同地点加10分
        score = score.add(BigDecimal.valueOf(matchingPlaces * 10));
        return score;
    }
    
    // 互补匹配实现
    private BigDecimal calculateComplementMatchScore(UserProfile p1, UserProfile p2) {
        // 基础分50分
        BigDecimal score = BigDecimal.valueOf(50);
        
        // 防空判断
        if(p1.getGoodSubjects() == null || p1.getNeedSubjects() == null || 
           p2.getGoodSubjects() == null || p2.getNeedSubjects() == null) {
            return BigDecimal.ZERO;
        }
        
        // 1. 检查互补性
        List<Integer> p1Good = p1.getGoodSubjects();
        List<Integer> p1Need = p1.getNeedSubjects();
        List<Integer> p2Good = p2.getGoodSubjects();
        List<Integer> p2Need = p2.getNeedSubjects();
        
        // 计算互补匹配度
        long complementCount = p1Good.stream()
                .filter(p2Need::contains)
                .count();
        complementCount += p2Good.stream()
                .filter(p1Need::contains)
                .count();
                
        score = score.add(BigDecimal.valueOf(complementCount * 15));
        
        // 2. 时间匹配度(30%)
        if(p1.getAvailableTimes() != null && p2.getAvailableTimes() != null) {
            List<TimeSlot> p1Times = JsonTimeUtil.convertToTimeSlots(p1.getAvailableTimes());
            List<TimeSlot> p2Times = JsonTimeUtil.convertToTimeSlots(p2.getAvailableTimes());
            
            long matchingTimeSlots = p1Times.stream()
                    .filter(t1 -> p2Times.stream()
                            .anyMatch(t2 -> t2.getWeekday().equals(t1.getWeekday()) 
                                    && hasTimeOverlap(t1.getTime(), t2.getTime())))
                    .count();
            score = score.add(BigDecimal.valueOf(matchingTimeSlots * 10));
        }
        
        return score;
    }
    
    // 辅助方法：检查时间段是否重合
    private boolean hasTimeOverlap(String time1, String time2) {
        String[] t1 = time1.split("-");
        String[] t2 = time2.split("-");
        
        int t1Start = parseTime(t1[0]);
        int t1End = parseTime(t1[1]);
        int t2Start = parseTime(t2[0]);
        int t2End = parseTime(t2[1]);
        
        return !(t1End <= t2Start || t2End <= t1Start);
    }
    
    // 辅助方法：将HH:mm格式转换为分钟数
    private int parseTime(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private MatchVO createMatchVO(UserProfile p1, UserProfile p2, String type, 
            Integer subjectId, Integer placeId, BigDecimal score) {
        MatchVO vo = new MatchVO();
        vo.setUserId1(p1.getUserId());
        vo.setUserId2(p2.getUserId());
        vo.setMatchType(type);
        vo.setSubjectId(subjectId);
        vo.setPlaceId(placeId);
        vo.setMatchScore(score);
        
        // 填充用户名等信息
        Account a1 = accountService.getById(p1.getUserId());
        Account a2 = accountService.getById(p2.getUserId());
        if(a1 != null) vo.setUsername1(a1.getUsername());
        if(a2 != null) vo.setUsername2(a2.getUsername());
        
        return vo;
    }

    @Override
    public List<MatchVO> getUserMatches() {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        return this.baseMapper.getUserMatches(userId).stream()
                .map(this::convertToMatchVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchVO> getPendingMatches() {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        return this.baseMapper.getPendingMatches(userId).stream()
                .map(this::convertToMatchVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean confirmMatch(Integer id) {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        return this.baseMapper.confirmMatch(id, userId) > 0;
    }

    @Override
    public boolean rejectMatch(Integer id) {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        return this.baseMapper.rejectMatch(id, userId) > 0;
    }

    @Override
    public boolean completeMatch(Integer id) {
            Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
            return this.baseMapper.completeMatch(id, userId) > 0;
        }

        private MatchVO convertToMatchVO(MatchRecord record) {
        MatchVO vo = new MatchVO();
        // 复制基础字段
        BeanUtils.copyProperties(record, vo);
        
        // 填充用户名
        Account user1 = accountService.getById(record.getUserId1());
        Account user2 = accountService.getById(record.getUserId2());
        if(user1 != null) vo.setUsername1(user1.getUsername());
        if(user2 != null) vo.setUsername2(user2.getUsername());
        
        // 填充科目名称
        if(record.getSubjectId() != null) {
            Subject subject = subjectService.getById(record.getSubjectId());
            if(subject != null) {
                vo.setSubjectName(subject.getName());
            }
        }
        
        // 填充场地名称
        if(record.getPlaceId() != null) {
            Place place = placeService.getById(record.getPlaceId());
            if(place != null) {
                vo.setPlaceName(place.getName());
            }
        }
        
        return vo;
    }

    // 通用匹配方法
    private List<MatchVO> doMatch(String matchType, 
            Function<UserProfile, List<MatchVO>> matchFunction) {
        // 1. 获取当前用户
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        UserProfile profile = userProfileService.getUserProfile(userId);
        if(profile == null) {
            return Collections.emptyList();
        }

        // 2. 获取已有匹配记录
        List<MatchRecord> existingMatches = this.lambdaQuery()
                .eq(MatchRecord::getUserId1, userId)
                .eq(MatchRecord::getMatchType, matchType)
                .list();

        // 3. 执行匹配
        List<MatchVO> matches = matchFunction.apply(profile);
        
        // 4. 按分数排序
        matches.sort((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()));
        
        // 5. 如果有未匹配过的，返回分数最高的
        MatchVO bestMatch = matches.stream()
                .filter(m -> !existingMatches.stream()
                        .anyMatch(e -> e.getUserId2().equals(m.getUserId2())))
                .findFirst()
                .orElse(null);
                
        if(bestMatch != null) {
            return Collections.singletonList(bestMatch);
        }

        // 6. 如果都匹配过了，随机返回一个历史最高分匹配
        if(!matches.isEmpty()) {
            int randomIndex = (int)(Math.random() * Math.min(3, matches.size()));
            return Collections.singletonList(matches.get(randomIndex));
        }

        return Collections.emptyList();
    }
}
