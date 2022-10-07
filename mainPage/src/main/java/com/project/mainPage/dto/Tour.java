package com.project.mainPage.dto;

import java.util.List;

import lombok.Data;

/*
 *   desc tour;
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| tour_rank   | int          | NO   | PRI | NULL    | auto_increment |
| tourist     | varchar(255) | NO   |     | NULL    |                |
| province    | varchar(255) | NO   |     | NULL    |                |
| city        | varchar(255) | NO   |     | NULL    |                |
| address3    | varchar(255) | NO   |     | NULL    |                |
| category_id | varchar(255) | YES  | MUL | NULL    |                |
| search      | int          | NO   |     | NULL    |                |
| views       | int          | NO   |     | 0       |                |
| user_id     | varchar(45)  | NO   | MUL | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
11 rows in set (0.00 sec)
 * */
@Data
public class Tour {
	 private int tour_rank;
	 private String tourist;
	 private String province;
	 private String city;
	 private String address3;
	 private Category category;
	 private int views;
	 private int search;
	 private int ranking;
	 private UserDto user; // UsersDto.user_id : fk 
	 private List<TourImg> tourImgs; // 1:N  TourImg.tour_rank fk
}
