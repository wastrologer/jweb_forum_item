package com.dao.mapper;

import com.pojo.Collection;
import com.pojo.Essay;
import com.pojo.EssayParam;
import com.pojo.User;

import java.util.List;

public interface EssayMapper {
    public List<Essay> getEssayByCollectionCondition(Collection collection);
    public List<Essay> getEssayByUserList(List<Integer> list);
    public Essay getEssayComments();
    public Integer countEssayByCondition(Essay essay);
    public List<Essay> getEssayByAccurateCondition(Essay essay);
    public List<Essay> getEssayByFuzzyCondition(Essay essay);
    public List<Essay> getEssayByCondition(EssayParam essayParam);
    public List<Essay> getEssayOrderByTime(Essay essay);
    public List<Essay> getEssayOrderByClick(Essay essay);
    public Integer addEssay(Essay essay);
    public Integer updateEssay(Essay essay);
    public Integer updateEssayBatch(User user);
    public Integer deleteEssayById(Integer id);

}
