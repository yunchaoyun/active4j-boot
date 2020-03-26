package com.zhonghe.active4j.func.timer.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhonghe.active4j.core.util.DateUtils;
import com.zhonghe.active4j.func.timer.dao.QuartzJobLogDao;
import com.zhonghe.active4j.func.timer.entity.QuartzJobLogEntity;
import com.zhonghe.active4j.func.timer.service.QuartzJobLogService;
import com.zhonghe.active4j.system.util.SystemUtils;

/**
 * 
 * @title QuartzJobLogServiceImpl.java
 * @description 
		定时任务日志
 * @time  2019年12月10日 上午10:00:45
 * @author guyp
 * @version 1.0
 */
@Service("quartzJobLogService")
@Transactional
public class QuartzJobLogServiceImpl extends ServiceImpl<QuartzJobLogDao, QuartzJobLogEntity> implements QuartzJobLogService {

	/**
	 * 
	 * @description
	 *  	删除定时任务日志
	 * @params
	 *      ids 日志ids
	 * @return void
	 * @author guyp
	 * @time 2019年12月12日 上午11:00:53
	 */
	public void delLogs(String ids) {
		List<String> lstIds = new ArrayList<String>();
		//“,”分割成数组
		String[] strIds = ids.split(",");
		if(null != strIds && strIds.length > 0) {
			for(String id : strIds) {
				//组装list
				lstIds.add(id);
			}
		}
		//删除
		this.removeByIds(lstIds);
	}
	
	/**
	 * 
	 * @description
	 *  	清空日志表
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月12日 上午11:14:27
	 */
	public void emptyLogs() {
		this.remove(new QueryWrapper<QuartzJobLogEntity>());
	}

	/**
	 * 
	 * @description
	 *  	获取日志明细
	 * @params
	 *      id 日志id
	 * @return Model
	 * @author guyp
	 * @time 2019年12月12日 上午11:03:40
	 */
	public Model getLogDetailModel(String id, Model model) {
		//根据id获取任务实体
		QuartzJobLogEntity log = this.getById(id);
		if(null != log) {
			//任务分组的处理
			log.setJobGroup(SystemUtils.getDictionaryValue("func_timer_job_group", log.getJobGroup()));
			//任务执行状态的处理
			log.setStatus(SystemUtils.getDictionaryValue("func_timer_job_log_status", log.getStatus()));
			//开始执行时间的处理
			if(null != log.getStartTime()) {
				model.addAttribute("startTime", DateUtils.getDate2Str(log.getStartTime()));
			}
			//结束执行时间的处理
			if(null != log.getEndTime()) {
				model.addAttribute("endTime", DateUtils.getDate2Str(log.getEndTime()));
			}
			//赋值
			model.addAttribute("log", log);
		}
		return model;
	}

}
