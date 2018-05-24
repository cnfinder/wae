package cn.finder.wx.response;



public class WxOrderSearchResponse extends WeixinResponse{

	
	//返回结果
		private String return_code;
		
		private String return_msg;
		
		//以下字段在return_code为SUCCESS的时候有返回
		
		private String appid;
		
		private String mch_id;
		private String nonce_str;
		
		private String sign;
		private String result_code;
		
		private String err_code;
		
		private String err_code_des;

		
		
		
		
		//以下字段在return_code 和result_code都为SUCCESS的时候有返回===========
		
		
		private String device_info;
		
		private String openid;
		
		
		
		private String trade_type;

		private String trade_state;
		private String bank_type;
		
		private String bank_type_des;
		
		private int total_fee;
		
		private int settlement_total_fee;
		private String fee_type;
		private int cash_fee;
		
		private String cash_fee_type;
		private String out_trade_no;
		
		private String trade_state_desc;
		
		private String is_subscribe;
		
		private String transaction_id;
		
		
		private String attach;
		
		private String time_end;
		

		
		
		public String getAttach() {
			return attach;
		}

		public void setAttach(String attach) {
			this.attach = attach;
		}

		public String getTime_end() {
			return time_end;
		}

		public void setTime_end(String time_end) {
			this.time_end = time_end;
		}

		public String getTransaction_id() {
			return transaction_id;
		}

		public void setTransaction_id(String transaction_id) {
			this.transaction_id = transaction_id;
		}

		public String getBank_type() {
			return bank_type;
		}

		public void setBank_type(String bank_type) {
			this.bank_type = bank_type;
		}

		public int getSettlement_total_fee() {
			return settlement_total_fee;
		}

		public void setSettlement_total_fee(int settlement_total_fee) {
			this.settlement_total_fee = settlement_total_fee;
		}

		public String getCash_fee_type() {
			return cash_fee_type;
		}

		public void setCash_fee_type(String cash_fee_type) {
			this.cash_fee_type = cash_fee_type;
		}

		public String getIs_subscribe() {
			return is_subscribe;
		}

		public void setIs_subscribe(String is_subscribe) {
			this.is_subscribe = is_subscribe;
		}

		public String getReturn_code() {
			return return_code;
		}
		
		

		public String getBank_type_des() {
			return bank_type_des;
		}

		public void setBank_type_des(String bank_type_des) {
			this.bank_type_des = bank_type_des;
		}

		public void setReturn_code(String return_code) {
			this.return_code = return_code;
		}

		public String getReturn_msg() {
			return return_msg;
		}

		public void setReturn_msg(String return_msg) {
			this.return_msg = return_msg;
		}

		public String getAppid() {
			return appid;
		}

		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getMch_id() {
			return mch_id;
		}

		public void setMch_id(String mch_id) {
			this.mch_id = mch_id;
		}

		public String getNonce_str() {
			return nonce_str;
		}

		public void setNonce_str(String nonce_str) {
			this.nonce_str = nonce_str;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getResult_code() {
			return result_code;
		}

		public void setResult_code(String result_code) {
			this.result_code = result_code;
		}

		public String getErr_code() {
			return err_code;
		}

		public void setErr_code(String err_code) {
			this.err_code = err_code;
		}

		public String getErr_code_des() {
			return err_code_des;
		}

		public void setErr_code_des(String err_code_des) {
			this.err_code_des = err_code_des;
		}

		public String getDevice_info() {
			return device_info;
		}

		public void setDevice_info(String device_info) {
			this.device_info = device_info;
		}

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}

		public String getTrade_type() {
			return trade_type;
		}

		public void setTrade_type(String trade_type) {
			this.trade_type = trade_type;
		}

		public String getTrade_state() {
			return trade_state;
		}

		public void setTrade_state(String trade_state) {
			this.trade_state = trade_state;
		}

		public int getTotal_fee() {
			return total_fee;
		}

		public void setTotal_fee(int total_fee) {
			this.total_fee = total_fee;
		}

		public String getFee_type() {
			return fee_type;
		}

		public void setFee_type(String fee_type) {
			this.fee_type = fee_type;
		}

		public int getCash_fee() {
			return cash_fee;
		}

		public void setCash_fee(int cash_fee) {
			this.cash_fee = cash_fee;
		}

		public String getOut_trade_no() {
			return out_trade_no;
		}

		public void setOut_trade_no(String out_trade_no) {
			this.out_trade_no = out_trade_no;
		}

		public String getTrade_state_desc() {
			return trade_state_desc;
		}

		public void setTrade_state_desc(String trade_state_desc) {
			this.trade_state_desc = trade_state_desc;
		}
		

		@Override
		public boolean isSuccess() {
			if("SUCCESS".equalsIgnoreCase(return_code))
				return true;
			return false;
		}

		
}
