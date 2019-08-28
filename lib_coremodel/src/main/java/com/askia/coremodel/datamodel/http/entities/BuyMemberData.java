package com.askia.coremodel.datamodel.http.entities;

public class BuyMemberData {

    /**
     * code : 1000
     * msg : 百盟优品已生成购买会员预付单！！！
     * list : null
     * obj : {"payType":100270,"out_trade_no":"DD70302418070217070000000659","total_amount":0.01,"body":"百盟优品购买会员","rqcode_url":"http://www.zjzfpay.com/plugins/payment/index/index/out_trade_no/cti7022244214963d.html"}
     */

    private int code;
    private String msg;
    private Object list;
    private ObjBean obj;

    public boolean isError() {
        return code != ResponseCode.ResponseSuccessCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * payType : 100270
         * out_trade_no : DD70302418070217070000000659
         * total_amount : 0.01
         * body : 百盟优品购买会员
         * rqcode_url : http://www.zjzfpay.com/plugins/payment/index/index/out_trade_no/cti7022244214963d.html
         */

        private int payType;
        private String out_trade_no;
        private double total_amount;
        private String body;
        private String rqcode_url;

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getRqcode_url() {
            return rqcode_url;
        }

        public void setRqcode_url(String rqcode_url) {
            this.rqcode_url = rqcode_url;
        }
    }
}
