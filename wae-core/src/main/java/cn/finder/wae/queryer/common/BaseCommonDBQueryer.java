package cn.finder.wae.queryer.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.comm.Reflection;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.DBType;
import cn.finder.wae.common.exception.ChainStopException;
import cn.finder.wae.common.exception.InfoException;
import cn.finder.wae.common.exception.WaeRuntimeException;
import cn.finder.wae.queryer.Queryer;
import cn.finder.wae.queryer.handleclass.QueryerAfterClass;
import cn.finder.wae.queryer.handleclass.QueryerBeforeClass;


/*****
 * 
 * @author wuhualong
 *
 */
public abstract class BaseCommonDBQueryer extends BaseJdbcDaoSupport implements Queryer {


	
	/***
	 * MongoDB 包装
	 * @param sql
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	protected String wrapperPagerSqlForMongoDB(String sql, int pageIndex, int pageSize) {
		 Query query = new Query();
		 query.skip((pageIndex-1)*pageSize);
		 query.limit(pageSize);
		 
		 CriteriaDefinition cd=new TextCriteria();
		 
		String sql_total = sql.replaceFirst("select ", "select top 999999999999999 ");
		StringBuffer sql_pager = new StringBuffer();
		sql_pager.append("select * from (")
			.append(" select row_number()over(order by TempColmun) rowNumber,* from (")
			.append(" select top ").append(pageIndex*pageSize).append(" 0 TempColmun,* from (")
			.append(sql_total).append(")t1 ) t2) t3 ")
			.append("where rowNumber>").append((pageIndex-1)*pageSize);

		return sql_pager.toString();
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
							logger.warn("======"+frontClassItem+ " 反射异常,详细信息:"+e);
						continue;
					}		
					if(queryerBeforeClass!=null)
						queryerBeforeClass.handle(showTableConfigId, condition);
				}
			}
			
			//先调用 不受spring 管理的业务
			//queryResult = queryTableQueryResultNoTransaction(showTableConfigId, condition);
			
			//if(queryResult==null){
				//主题操作
				queryResult = queryTableQueryResult(showTableConfigId, condition);
			//}
			
		
			

			
			
			
			//后置操作
			if(!StringUtils.isEmpty(showTableConfig.getGridrowBackCmd())){
				String[] rowBackCmdArr = showTableConfig.getGridrowBackCmd().split(",");
				for(String rowBackCmdItem : rowBackCmdArr)
				{
				
					/*QueryerAfterClass queryerAfterClass=null;
						queryerAfterClass = (QueryerAfterClass) reflect.newInstance(rowBackCmdItem,
								new Object[] {});
						
						
					
					if(queryerAfterClass!=null)
						queryResult=queryerAfterClass.handle(queryResult, showTableConfigId, condition);*/
					
					
					
					QueryerAfterClass queryerAfterClass=null;
					try {
						queryerAfterClass = (QueryerAfterClass) reflect.newInstance(rowBackCmdItem,
								new Object[] {});
						
					} catch (ClassNotFoundException e) {
						if(rowBackCmdItem.toLowerCase().indexOf("javascript:")==-1)
							logger.warn("======"+rowBackCmdItem+ " 反射异常,详细信息:"+e);
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
			String stackTrace=Common.getExceptionStackTrace(e);
			logger.error(Common.getExceptionStackTrace(e));
			WaeRuntimeException exception=new WaeRuntimeException("server is error",e);
			exception.getErrInfo().setErrMsg(stackTrace);
			
			//logger.error("==="+getClassMethodInfo(e.getStackTrace()));
			//返回给上层
			throw exception;
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
	
	
}
