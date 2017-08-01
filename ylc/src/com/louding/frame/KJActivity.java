/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.louding.frame;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.louding.frame.ui.FrameActivity;
import com.louding.frame.ui.I_KJActivity;
import com.louding.frame.ui.KJActivityStack;
import com.louding.frame.utils.KJLoger;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.StartApplication;
import com.nangua.xiaomanjflc.UmengManager;
import com.nangua.xiaomanjflc.YilicaiApplication;
import com.nangua.xiaomanjflc.support.ScreenObserver.ScreenStateListener;
import com.nangua.xiaomanjflc.ui.AccountActivity;
import com.nangua.xiaomanjflc.ui.GestureActivity;
import com.nangua.xiaomanjflc.ui.VerifyPwd;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public abstract class KJActivity extends FrameActivity implements ScreenStateListener, I_KJActivity{

    /**
     * 当前Activity状态
     */
    public static enum ActivityState {
        RESUME, PAUSE, STOP, DESTROY
    }

    public Activity aty;
    /** Activity状态 */
    public ActivityState activityState = ActivityState.DESTROY;

    /***************************************************************************
     * 
     * print Activity callback methods
     * 
     ***************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        aty = this;
        YilicaiApplication.getInstance().setActivity(this);
        YilicaiApplication.getInstance().addStackActivity(this);
        KJActivityStack.create().addActivity(this);
        KJLoger.state(this.getClass().getName(), "---------onCreat ");
        PushAgent.getInstance(this).onAppStart();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        KJLoger.state(this.getClass().getName(), "---------onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
      //Umeng活动监听resume
        if (UmengManager.analyticsStatus == UmengManager.AnalyticsOn) {
	    	String name = this.getClass().getSimpleName();
	    	MobclickAgent.onPageStart(name); //统计页面，"MainScreen"为页面名称，可自定义
	    	MobclickAgent.onResume(this);
	    }
        YilicaiApplication.getInstance().setCurrentRunningActivity(this);
        activityState = ActivityState.RESUME;
        KJLoger.state(this.getClass().getName(), "---------onResume ");
        //如下activity 不参与 手势判定验证
        if(!(aty instanceof StartApplication) &&
           !(aty instanceof VerifyPwd) &&
           !(aty instanceof AccountActivity) &&
           ApplicationUtil.isNeedGesture(this)){
        	startActivity(new Intent(this, GestureActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Umeng活动监听pause
        if (UmengManager.analyticsStatus == UmengManager.AnalyticsOn) {
	    	String name = this.getClass().getSimpleName();
	    	MobclickAgent.onPageEnd(name); 
	    	MobclickAgent.onPause(this);
	    }
        if (YilicaiApplication.getInstance().getCurrentRunningActivity().equals(this)) {
            YilicaiApplication.getInstance().setCurrentRunningActivity(null);
        }
        activityState = ActivityState.PAUSE;
        KJLoger.state(this.getClass().getName(), "---------onPause ");
        AppVariables.lastTime = new Date().getTime(); 
    }

    @Override
    protected void onStop() {
//        super.onResume();
        super.onStop();
        activityState = ActivityState.STOP;
        KJLoger.state(this.getClass().getName(), "---------onStop ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        KJLoger.state(this.getClass().getName(), "---------onRestart ");
    }

    @Override
    protected void onDestroy() {
        activityState = ActivityState.DESTROY;
        KJLoger.state(this.getClass().getName(), "---------onDestroy ");
        super.onDestroy();
        KJActivityStack.create().finishActivity(this);
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    @Override
    public void skipActivity(Activity aty, Class<?> cls) {
        showActivity(aty, cls);
        aty.finish();
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    @Override
    public void skipActivity(Activity aty, Intent it) {
        showActivity(aty, it);
        aty.finish();
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    @Override
    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        showActivity(aty, cls, extras);
        aty.finish();
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    @Override
    public void showActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    @Override
    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    @Override
    public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

	@Override
	public void onScreenOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScreenOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserPresent() {
		// TODO Auto-generated method stub
		
	}
    
}
