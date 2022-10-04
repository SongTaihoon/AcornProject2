package com.project.mainPage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.mainPage.dto.Rest;
import com.project.mainPage.mapper.RestMapper;

@Service
public class RestService {
	@Autowired
	private RestMapper restMapper;
	
	public Rest restUpdateView(int restRank) throws Exception{
		restMapper.updateViews(restRank);
		return restMapper.selectDetailOne(restRank);
	}
}
