package com.example.testar.utils;

public interface TaskListener {
	public void onSuccess(String content);
	public void onFailure(ErrorCode errCode);
}
