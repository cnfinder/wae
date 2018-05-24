package cn.finder.wae.common.comm;

import java.sql.Date;

import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

public class DateJsonConfig extends JsonConfig{
	public DateJsonConfig(){
		this.setIgnoreDefaultExcludes(false);
		this.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		this.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor());
	}
}
