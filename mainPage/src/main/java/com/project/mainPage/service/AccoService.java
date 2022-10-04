package com.project.mainPage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.mainPage.dto.Acco;
import com.project.mainPage.mapper.AccoMapper;

@Service
public class AccoService {
	@Autowired
	private AccoMapper accoMapper;
	
	public Acco accoUpdateView(int accoRank) throws Exception{
		return accoMapper.selectDetailOne(accoRank);
	}
}
