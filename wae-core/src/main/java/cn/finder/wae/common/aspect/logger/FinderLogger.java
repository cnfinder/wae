package cn.finder.wae.common.aspect.logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FinderLogger {

	
	String name() default "";  //操作名称,如果名称为空，那么试着从 showtableConfig 中获取,否则为""
	
	ArchType archType() default ArchType.NORMAL; //默认是基础架构
	
	String[] argNames() default ""; //参数名称,按照参数顺序注明参数名称，ArchType参数可以不用注明
	
}
