package com.jackhxs.data;

import org.json.JSONObject;

public interface GetJSONListener {
	public void onRemoteCallComplete(JSONObject jsonFromNet);
}