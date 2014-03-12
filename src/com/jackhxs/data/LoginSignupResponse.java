package com.jackhxs.data;

public class LoginSignupResponse {
	private String status;
	private String userId;
	private String sessionId;
	private String err;
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
	
}





/*
{
    "status": "failure",
    "err": {
        "name": "MongoError",
        "err": "E11000 duplicate key error index: heroku_app17074892.users.$email_1  dup key: { : \"orbar1@gmail.com\" }",
        "code": 11000,
        "n": 0,
        "lastOp": "0",
        "connectionId": 594741,
        "ok": 1
    }
}

{
    "status": "success",
    "userId": "52f2a2c504858d0200000003",
    "sessionId": "wu%2F4x2sw51ZhNsbl6vBbJbqi"
}

*/