package com.example.udong_mp2019;

import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.udong_mp2019.login.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void login_and_listDisplayed(){

        onView(withId(R.id.btn_signin)).perform(ViewActions.click());

        //로그인에 힌트가 보이는지 확인
        onView(withId(R.id.et_email)).check(matches(withHint(R.string.signin_email_hint)));

        // 로그인 수행
        onView(withId(R.id.et_email)).perform(ViewActions.typeText("gyop0817@naver.com")); // ID 입력
        onView(withId(R.id.et_password)).perform(ViewActions.typeText("qwerqwert"),ViewActions.closeSoftKeyboard()); // 패스워드 입력
        onView(withId(R.id.btn_signin_submit)).perform(ViewActions.click()); // 로그인 클릭

        // 5초 기다린다. 그 전에 성공하면 넘어감.
        //onView(isRoot()).perform(waitId(R.id.CircleList, 5000));

        // 다음 화면으로 넘어가서 ViewPager가 제대로 나왔는지 확인.
        //onView(withId(R.id.customViewPagerMain)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void login_and_listDisplayed2(){

        onView(withId(R.id.btn_signin)).perform(ViewActions.click());

        //로그인에 힌트가 보이는지 확인
        onView(withId(R.id.et_email)).check(matches(withHint(R.string.signin_email_hint)));

        // 로그인 수행
        onView(withId(R.id.et_email)).perform(ViewActions.typeText("gyop0817@naver.com")); // ID 입력
        onView(withId(R.id.et_password)).perform(ViewActions.typeText("qwerqwer"),ViewActions.closeSoftKeyboard()); // 패스워드 입력
        onView(withId(R.id.btn_signin_submit)).perform(ViewActions.click()); // 로그인 클릭

        // 5초 기다린다. 그 전에 성공하면 넘어감.
        //onView(isRoot()).perform(waitId(R.id.CircleList, 5000));

        // 다음 화면으로 넘어가서 ViewPager가 제대로 나왔는지 확인.
        //onView(withId(R.id.customViewPagerMain)).check(ViewAssertions.matches(isDisplayed()));
    }
}
