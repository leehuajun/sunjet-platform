package com.sunjet.frontend.utils.activiti;

/**
 * 自定义审批意见
 * <p>
 * Created by lhj on 16/10/19.
 */
public class CustomComment {
//    private static final long serialVersionUID = 6493293663235903747L;

    private String result = "";      // 审核结果,比如同意/退回
    private String message = "";     // 审核意见

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomComment() {
    }

    public CustomComment(String result, String message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CustomComment{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
