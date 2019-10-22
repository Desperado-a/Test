package com.example.test;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FrameActivity extends FragmentActivity {

    private Fragment mFragment[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton rbthome,rbtfunc,rbtsetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mFragment=new Fragment[3];
        fragmentManager=getSupportFragmentManager();
        mFragment[0]=fragmentManager.findFragmentById(R.id.frag_main);
        mFragment[1]=fragmentManager.findFragmentById(R.id.frag_func);
        mFragment[2]=fragmentManager.findFragmentById(R.id.frag_setting);
        fragmentTransaction=fragmentManager.beginTransaction().hide(mFragment[0]).hide(mFragment[1]).hide(mFragment[2]);
        fragmentTransaction.show(mFragment[0]).commit();

        rbthome=(RadioButton)findViewById(R.id.radiohome);
        rbtfunc=(RadioButton)findViewById(R.id.radiofunc);
        rbtsetting=(RadioButton)findViewById(R.id.radiosetting);
        rbthome.setBackgroundResource(R.drawable.shape3);

        radioGroup=(RadioGroup)findViewById(R.id.bottomGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                fragmentTransaction=fragmentManager.beginTransaction().hide(mFragment[0]).hide(mFragment[1]).hide(mFragment[2]);
                rbthome.setBackgroundResource(R.drawable.shape2);
                rbtfunc.setBackgroundResource(R.drawable.shape2);
                rbtsetting.setBackgroundResource(R.drawable.shape2);
                switch (i){
                    case R.id.radiohome:
                        fragmentTransaction.show(mFragment[0]).commit();
                        rbthome.setBackgroundResource(R.drawable.shape3);
                        break;
                    case R.id.radiofunc:
                        fragmentTransaction.show(mFragment[1]).commit();
                        rbtfunc.setBackgroundResource(R.drawable.shape3);
                    break;
                    case R.id.radiosetting:
                        fragmentTransaction.show(mFragment[2]).commit();
                        rbtsetting.setBackgroundResource(R.drawable.shape3);
                        break;
                    default:break;
                }
            }
        });
    }
}
