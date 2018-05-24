package cn.finder.wae.common.aspect.logger;

/****
 * 标识是否是基础架构，不同的方法风格，使用不同的日志处理方式
 * @author wuhualong
 *
 */
public enum ArchType {
	NORMAL,//普通的方法调用
	
	FINDER_ARCH, //基础架构方式
	    
	OTHER     //其他架构方式
}
