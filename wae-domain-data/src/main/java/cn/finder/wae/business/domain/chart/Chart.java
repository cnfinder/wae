package cn.finder.wae.business.domain.chart;

import java.util.Date;

public class Chart {

	private long id;
	
	private String chart_data;
	
	private String name;
	
	private String title;
	
	private String sub_title;
	private long showtable_config_id_page;
	
	private long showtable_config_id;
	
	private Date create_date;
	
	private Date update_date;
	
	private String remark;
	
	private int enable_export;
	
	private int enable_print;
	private String credits;
	private String x_title;
	private String y_title;
	private String x_field;
	private String y_field;
	private String x_field_two;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getChart_data() {
		return chart_data;
	}
	public void setChart_data(String chart_data) {
		this.chart_data = chart_data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSub_title() {
		return sub_title;
	}
	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}
	public long getShowtable_config_id_page() {
		return showtable_config_id_page;
	}
	public void setShowtable_config_id_page(long showtable_config_id_page) {
		this.showtable_config_id_page = showtable_config_id_page;
	}
	public long getShowtable_config_id() {
		return showtable_config_id;
	}
	public void setShowtable_config_id(long showtable_config_id) {
		this.showtable_config_id = showtable_config_id;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getEnable_export() {
		return enable_export;
	}
	public void setEnable_export(int enable_export) {
		this.enable_export = enable_export;
	}
	public int getEnable_print() {
		return enable_print;
	}
	public void setEnable_print(int enable_print) {
		this.enable_print = enable_print;
	}
	public String getCredits() {
		return credits;
	}
	public void setCredits(String credits) {
		this.credits = credits;
	}
	public String getX_title() {
		return x_title;
	}
	public void setX_title(String x_title) {
		this.x_title = x_title;
	}
	public String getY_title() {
		return y_title;
	}
	public void setY_title(String y_title) {
		this.y_title = y_title;
	}
	public String getX_field() {
		return x_field;
	}
	public void setX_field(String x_field) {
		this.x_field = x_field;
	}
	public String getY_field() {
		return y_field;
	}
	public void setY_field(String y_field) {
		this.y_field = y_field;
	}
	public String getX_field_two() {
		return x_field_two;
	}
	public void setX_field_two(String x_field_two) {
		this.x_field_two = x_field_two;
	}
	
	
	
}
