package com.project.mainPage.dto;

import lombok.Data;

/*
 *  desc tour;
+-----------+--------------+------+-----+---------+-------+
| Field     | Type         | Null | Key | Default | Extra |
+-----------+--------------+------+-----+---------+-------+
| tour_rank | varchar(45)  | NO   | PRI | NULL    |       |
| tourist   | varchar(255) | NO   |     | NULL    |       |
| province  | varchar(255) | NO   |     | NULL    |       |
| city      | varchar(255) | NO   |     | NULL    |       |
| address3  | varchar(255) | NO   |     | NULL    |       |
| tours     | varchar(255) | NO   |     | NULL    |       |
| category  | varchar(255) | NO   |     | NULL    |       |
| search    | int          | NO   |     | NULL    |       |
| views     | int          | NO   |     | 0       |       |
| good      | int          | NO   |     | 0       |       |
| bad       | int          | NO   |     | 0       |       |
+-----------+--------------+------+-----+---------+-------+
*/
@Data
public class Tour {
	 private int tour_rank;
	 private String tourist;
	 private String province;
	 private String city;
	 private String address3;
	 private String rest;
	 private String category;
	 private int good;
	 private int bad;
	 private int views;
	 private int search;
}
