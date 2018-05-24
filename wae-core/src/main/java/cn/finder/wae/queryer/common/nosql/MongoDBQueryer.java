package cn.finder.wae.queryer.common.nosql;


import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.Reflection;
import cn.finder.wae.common.exception.ChainStopException;
import cn.finder.wae.common.exception.InfoException;
import cn.finder.wae.common.exception.WaeRuntimeException;
import cn.finder.wae.queryer.Queryer;
import cn.finder.wae.queryer.handleclass.QueryerAfterClass;
import cn.finder.wae.queryer.handleclass.QueryerBeforeClass;
import common.Logger;

public class MongoDBQueryer implements Queryer {

	
	private Logger logger=Logger.getLogger(MongoDBQueryer.class);
	
	private MongoTemplate mongoTemplate;
	
	public void setDataSource(MongoTemplate mongoTemplate){
		
		this.mongoTemplate=mongoTemplate;
	}
	
	/***
	 * 获取 MongoTemplate
	 * @return
	 */
	public MongoTemplate getMongoTemplate(){
		return this.mongoTemplate;
	}
	

	/***
	 * 调用主管理入口
	 * @param sql
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public TableQueryResult queryTableQueryResultManager(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		TableQueryResult queryResult=new  TableQueryResult();
		
		Reflection reflect = new Reflection();
		ShowTableConfig showTableConfig=ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
		//前置操作
		String frontClass=showTableConfig.getGridrowFrontCmd();
		
		try{
			if(!StringUtils.isEmpty(frontClass)){
				
				String[] frontClassArr = frontClass.split(",");
				for(String frontClassItem : frontClassArr)
				{
					QueryerBeforeClass queryerBeforeClass=null;
					try {
						queryerBeforeClass = (QueryerBeforeClass) reflect.newInstance(frontClassItem,
								new Object[] {});
						
					} catch (ClassNotFoundException e) {
						if(frontClassItem.toLowerCase().indexOf("javascript:")==-1)
							logger.error("======"+frontClassItem+ " 反射异常,详细信息:"+e);
						continue;
					}		
					if(queryerBeforeClass!=null)
						queryerBeforeClass.handle(showTableConfigId, condition);
				}
			}
			
			queryResult = queryTableQueryResult(showTableConfigId, condition);//主体业务
			
			//后置操作
			if(!StringUtils.isEmpty(showTableConfig.getGridrowBackCmd())){
				String[] rowBackCmdArr = showTableConfig.getGridrowBackCmd().split(",");
				for(String rowBackCmdItem : rowBackCmdArr)
				{
				
					QueryerAfterClass queryerAfterClass=null;
					try {
						queryerAfterClass = (QueryerAfterClass) reflect.newInstance(rowBackCmdItem,
								new Object[] {});
						
					} catch (ClassNotFoundException e) {
						if(rowBackCmdItem.toLowerCase().indexOf("javascript:")==-1)
							logger.error("======"+rowBackCmdItem+ " 反射异常,详细信息:"+e);
						continue;
					}		
					if(queryerAfterClass!=null)
						queryResult=queryerAfterClass.handle(queryResult, showTableConfigId, condition);
				}
			}
			
		}
		
		catch(ChainStopException chainStopException){
			//此异常不需要处理
			logger.debug("===ChainStopException"+ chainStopException.toString());
			
			
		}
		
		catch(InfoException infoExp)
		{
			throw infoExp;
		}
		catch(Throwable e){
			logger.error(e.toString());
			logger.error("==="+getClassMethodInfo(e.getStackTrace()));
			//返回给上层
			throw new WaeRuntimeException(e);
		}
		
		
		
		
		return queryResult;
	}
	

	public static String getFailureMessage(String... parms) {  
        StringBuffer result = new StringBuffer("rest 调用");  
  
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();  
        String methodString = getClassMethodInfo(elements);  
        result.append(methodString);  
  
        result.append(" 方法，失败。");  
        if (parms != null) {  
            result.append(" 参数列表 parms:");  
            for (String parm : parms) {  
                result.append(parm + ",");  
            }  
        }  
        return result.toString();  
    }  
  
    private static String getClassMethodInfo(StackTraceElement[] elements) {  
        String result = "";  
        int len = null == elements ? 0 : elements.length - 1;  
        if (len > 2) {  
            String methodString = "";  
            StackTraceElement element = elements[2];  
            String shortClassName = getShortClassName(element.getClassName());  
            methodString += shortClassName + "." + element.getMethodName()  
                    + " ";  
            result = methodString.substring(0, methodString.length() - 1);  
        }  
        return result;  
    }  
  
    /** 
     * 获取短类名称（去掉包后的类名） 
     *  
     * @param className 
     * @return 
     */  
    private static String getShortClassName(String className) {  
        String result = "";  
        if (!StringUtils.isEmpty(className)) {  
            result = className.substring(className.lastIndexOf(".") + 1);  
        }  
        return result;  
    }  
	
	
	/***
	 * 
	 * @author: wuhualong
	 * @data:2014-5-5上午10:54:04
	 * @function:  主要为了包装  函数名称
	 * @param tableName
	 * @param tableType
	 * @return
	 */
	protected String wrapperTableName(String tableName,int tableType){
		if(tableType == 8){
			//函数
			return tableName+"()";
		}
		return tableName;
	}
	
	
	
	
	/***
	 * 这样做法不正确 ， 事务是在service中切入的 所有这个方法放在这边不需要
	 * 不在事务中执行  不受 spring 事务管理
	 * @param showTableConfigId
	 * @param condition
	 * @return if null-> 将执行 queryTableQueryResult 
	 *         如果 需要在事务中执行，那么子类只需要实现 queryTableQueryResult即可，
	 *          否则 子类 重写queryTableQueryResultNoTransaction 并且不应返回 为null，
	 *          而应该返回TableQueryResult对象
	 */
/*	@Deprecated
	public  TableQueryResult queryTableQueryResultNoTransaction(long showTableConfigId,
			QueryCondition<Object[]> condition){
		return null;
	}
	*/
	/***
	 * 默认在事务中执行  由 spring 事务管理器管理
	 * @param showTableConfigId
	 * @param condition
	 * @return
	 */
	public  TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		return null;
	}


	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		throw new RuntimeException("[setJDBCDataSource] this method is not allow used in mongo database");
	}

}
