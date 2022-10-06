package com.project.mainPage.dto;

import java.util.List;

import lombok.Data;

/*
 desc accommodate;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| acco_rank   | int          | NO   | PRI | NULL    | auto_increment |
| tourist     | varchar(255) | NO   |     | NULL    |                |
| province    | varchar(255) | NO   |     | NULL    |                |
| city        | varchar(255) | NO   |     | NULL    |                |
| address1    | varchar(255) | NO   |     | NULL    |                |
| search      | int          | NO   |     | NULL    |                |
| user_id     | varchar(45)  | NO   | MUL | NULL    |                |
| category_id | varchar(255) | YES  | MUL | NULL    |                |
| views       | int          | NO   |     | 0       |                |
+-------------+--------------+------+-----+---------+----------------+
*/
@Data
public class Acco {
	 private int acco_rank;
	 private String tourist;
	 private String province;
	 private String city;
	 private String address1;
	 private String acco;
	 private Category category;
	 private int views;
	 private int search;
	 private int ranking;
	 private UserDto user; // UsersDto.userid : fk 
	 private List<AccoImg> accoImgs; // 1:N  TourImg.tour_rank fk
}
