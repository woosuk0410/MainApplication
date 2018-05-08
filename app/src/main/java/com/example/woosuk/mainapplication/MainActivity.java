package com.example.woosuk.mainapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.library.MyClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            InputStream inputStream = getAssets().open("sub.apk");
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getExternalFilesDir(null) + "/sub.apk"));
            int length;
            byte[] buffer = new byte[1024];
            while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        DexClassLoader dexClassLoader = new DexClassLoader((getExternalFilesDir(null) + "/sub.apk"), "",null, ClassLoader.getSystemClassLoader());

        try {
            Class<?> _class = dexClassLoader.loadClass("com.example.woosuk.subapplication.MyClassInterface");
            Object object = _class.newInstance();

            Method methodGetIntValue = _class.getMethod("getIntValue");
            Log.e("TEST", "getIntValue return " + methodGetIntValue.invoke(object));//working fine

            Method methodGetMyClassInstance = _class.getMethod("getMyClassInstance");
            MyClass myClass = (MyClass) methodGetMyClassInstance.invoke(object);//java.lang.ClassCastException: com.example.library.TestLibraryClass cannot be cast to com.example.library.TestLibraryClass

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
