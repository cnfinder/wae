package cn.finder.wae.business.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class ShowTableConfig extends BaseDomain{

	private long id;
	
	private String name;
	
	private String showTableName;
	
	private int showType;
	
	private String sqlConfig;
	
	private int pageSize;
	
	
	private String remark;
	
	private List<ShowDataConfig> showDataConfigs;

	private String processCommand;
	
	private List<GridMenu> toolBarMenus;
	
	private List<GridMenu> rowFrontMenus;
	
	private List<GridMenu> rowBackMenus;
	
	private List<GridMenu> contextMenus;
	
	private String gridrowFrontCmd;
	
	private String gridrowBackCmd;
	
	private String resultName;
	
	private String childCmd;
	
	private long childShowtableConfigId;
	
	private long addShowtableConfigId;
	
	private long editShowtableConfigId;
	
	private long searchShowtableConfigId;
	
	private long deleteShowtableConfigId;
	
	private long targetDs;
	
	private int isMultiselect;
	
	private int useTransaction=1;
	@Deprecated
	private int isFirstQueryData=1;
	
	private int isPager=1;
	
	private String extProps;
	
	public long getId() {
		return id;
	}

	public ShowTableConfig() {
		super();
		toolBarMenus = new ArrayList<GridMenu>();
		rowFrontMenus = new ArrayList<GridMenu>();
		rowBackMenus = new ArrayList<GridMenu>();
		contextMenus = new ArrayList<GridMenu>();
		showDataConfigs=new ArrayList<ShowDataConfig>();
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getShowTableName() {
		return showTableName;
	}

	public void setShowTableName(String showTableName) {
		this.showTableName = showTableName;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public String getSqlConfig() {
		return sqlConfig;
	}

	public void setSqlConfig(String sqlConfig) {
		this.sqlConfig = sqlConfig;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ShowDataConfig> getShowDataConfigs() {
		return showDataConfigs;
	}

	public void setShowDataConfigs(List<ShowDataConfig> showDataConfigs) {
		this.showDataConfigs = showDataConfigs;
	}

	public String getProcessCommand() {
		return processCommand;
	}

	public void setProcessCommand(String processCommand) {
		this.processCommand = processCommand;
	}

	public List<GridMenu> getToolBarMenus() {
		return toolBarMenus;
	}

	public void setToolBarMenus(List<GridMenu> toolBarMenus) {
		this.toolBarMenus = toolBarMenus;
	}

	public List<GridMenu> getRowFrontMenus() {
		return rowFrontMenus;
	}

	public void setRowFrontMenus(List<GridMenu> rowFrontMenus) {
		this.rowFrontMenus = rowFrontMenus;
	}

	public List<GridMenu> getRowBackMenus() {
		return rowBackMenus;
	}

	public void setRowBackMenus(List<GridMenu> rowBackMenus) {
		this.rowBackMenus = rowBackMenus;
	}

	public String getGridrowFrontCmd() {
		return gridrowFrontCmd;
	}

	public void setGridrowFrontCmd(String gridrowFrontCmd) {
		this.gridrowFrontCmd = gridrowFrontCmd;
	}

	public String getGridrowBackCmd() {
		return gridrowBackCmd;
	}

	public void setGridrowBackCmd(String gridrowBackCmd) {
		this.gridrowBackCmd = gridrowBackCmd;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getChildCmd() {
		return childCmd;
	}

	public void setChildCmd(String childCmd) {
		this.childCmd = childCmd;
	}

	public long getChildShowtableConfigId() {
		return childShowtableConfigId;
	}

	public void setChildShowtableConfigId(long childShowtableConfigId) {
		this.childShowtableConfigId = childShowtableConfigId;
	}

	public long getAddShowtableConfigId() {
		return addShowtableConfigId;
	}

	public void setAddShowtableConfigId(long addShowtableConfigId) {
		this.addShowtableConfigId = addShowtableConfigId;
	}

	public long getEditShowtableConfigId() {
		return editShowtableConfigId;
	}

	public void setEditShowtableConfigId(long editShowtableConfigId) {
		this.editShowtableConfigId = editShowtableConfigId;
	}

	public long getSearchShowtableConfigId() {
		return searchShowtableConfigId;
	}

	public void setSearchShowtableConfigId(long searchShowtableConfigId) {
		this.searchShowtableConfigId = searchShowtableConfigId;
	}

	public long getTargetDs() {
		return targetDs;
	}

	public void setTargetDs(long targetDs) {
		this.targetDs = targetDs;
	}

	public long getDeleteShowtableConfigId() {
		return deleteShowtableConfigId;
	}

	public void setDeleteShowtableConfigId(long deleteShowtableConfigId) {
		this.deleteShowtableConfigId = deleteShowtableConfigId;
	}

	public int getIsMultiselect() {
		return isMultiselect;
	}

	public void setIsMultiselect(int isMultiselect) {
		this.isMultiselect = isMultiselect;
	}

	public List<GridMenu> getContextMenus() {
		return contextMenus;
	}

	public void setContextMenus(List<GridMenu> contextMenus) {
		this.contextMenus = contextMenus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUseTransaction() {
		return useTransaction;
	}

	public void setUseTransaction(int useTransaction) {
		this.useTransaction = useTransaction;
	}

	public int getIsFirstQueryData() {
		return isFirstQueryData;
	}

	public void setIsFirstQueryData(int isFirstQueryData) {
		this.isFirstQueryData = isFirstQueryData;
	}

	public int getIsPager() {
		return isPager;
	}

	public void setIsPager(int isPager) {
		this.isPager = isPager;
	}

	public String getExtProps() {
		return extProps;
	}

	public void setExtProps(String extProps) {
		this.extProps = extProps;
	}
	
	
	
	
	
	
}
