package com.example.sharedpreferencespractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText nameEditText, passwordEditText;
    Button loginBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.button_login);

        sharedPreferences = getSharedPreferences("Users", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = nameEditText.getText().toString();
                String passwordString = passwordEditText.getText().toString();

                // 쉐어드프리퍼런스 저장된 그 파일을 말함
                File file = new File(
                        // 쉐어드 저장된 파일에 오른쪽 마우스 클릭해서 경로보기? 그거 하면 나옴
                        "/data/data/com.example.sharedpreferencespractice/shared_prefs/Users.xml"
                );

                // 만약 이미 쉐어드 파일이 존재하면 = 추가된 회원이 한명 있으면
                if (file.exists()) {
                    Log.d(TAG, "========================");
                    Log.d(TAG, "file exists");

                    String savedSharedPrefString = sharedPreferences.getString("userKey", null);
                    Log.d(TAG, "========================");
                    Log.d(TAG, "savedSharedPrefString= " + savedSharedPrefString);

                    if (savedSharedPrefString != null) {
                        try {
                            JSONObject savedToObject = new JSONObject(savedSharedPrefString);
                            Log.d(TAG, "========================");
                            Log.d(TAG, "savedToObject = " + savedToObject);

                            JSONArray array = savedToObject.getJSONArray("USER");
                            Log.d(TAG, "========================");
                            Log.d(TAG, "array= " + array);

                            // W/System.err: org.json.JSONException: Value [{"name":"111","password":"1111"}] at USER of type org.json.JSONArray cannot be converted to JSONObject
//                            JSONObject object = savedToObject.getJSONObject("USER");
//                            Log.d(TAG, "========================");
//                            Log.d(TAG, "object= " + object);

                            // 이거 하면 [{"name":"111","password":"1111"}] ---> [[{"name":"111","password":"1111"}]]
//                            Iterator x = savedToObject.keys();
//                            JSONArray arrayFromObject = new JSONArray();
//                            while (x.hasNext()) {
//                                String key = (String) x.next();
//                                try {
//                                    arrayFromObject.put(savedToObject.get(key));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            Log.d(TAG, "========================");
//                            Log.d(TAG, "arrayFromObject= " + arrayFromObject);


                            JSONObject userObject = new JSONObject();
                            Log.d(TAG, "========================");
                            Log.d(TAG, "userObject (before put) = " + userObject);
                            try {
                                userObject.put("name", nameString);
                                userObject.put("password", passwordString);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "========================");
                            Log.d(TAG, "userObject (after put) = " + userObject);

                            array.put(userObject);
                            Log.d(TAG, "========================");
                            Log.d(TAG, "array = " + array);
                            // array = [{"name":"111","password":"1111"},{"name":"2222","password":"222"}]

                            JSONObject objectFromArray = new JSONObject();
                            try {
                                objectFromArray.put("USER", array);
                                Log.d(TAG, "========================");
                                Log.d(TAG, "objectFromArray= " + objectFromArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String finalString = objectFromArray.toString();
                            Log.d(TAG, "========================");
                            Log.d(TAG, "finalString= " + finalString);

                            editor.putString("userKey", finalString);
                            editor.commit();
                            Log.d(TAG, "========================");
                            Log.d(TAG, "updated_shared_pref_value= " + sharedPreferences.getString("userKey", null));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }





                    // W/System.err: org.json.JSONException: Value {"USER":[{"name":"111","password":"1111"}]} of type org.json.JSONObject cannot be converted to JSONArray
//                    JSONArray jsonArray;
//                    try {
//                        jsonArray = new JSONArray(savedSharedPrefString);
//                        Log.d(TAG, "========================");
//                        Log.d(TAG, "jsonArray (before put) = " + jsonArray);
//                        jsonArray.put(userObject);
//                        Log.d(TAG, "========================");
//                        Log.d(TAG, "jsonArray (after put) = " + jsonArray);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }


//                            JSONObject loadedObject = new JSONObject(savedSharedPrefString);
//                            Log.d(TAG, "========================");
//                            Log.d(TAG, "loadedObject = " + loadedObject);
////                            loadedObject.put("name", nameString);
////                            loadedObject.put("password", passwordString);
////                            Log.d(TAG, "========================");
////                            Log.d(TAG, "loadedObject (after put) = " + loadedObject);
//
//                            JSONObject newJSONObject = new JSONObject();
//                            try {
//                                newJSONObject.put("name", nameString);
//                                newJSONObject.put("password", passwordString);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            Log.d(TAG, "========================");
//                            Log.d(TAG, "newJSONObject= " + newJSONObject);

//                    editor.putString("userKey", jsonArray.toString());
//                    editor.commit();
//                    Log.d(TAG, "========================");
//                    Log.d(TAG, "saved sharedpref value= " + sharedPreferences.getString("userKey", ""));
                    nameEditText.setText("");
                    passwordEditText.setText("");

                // 추가된 회원이 없으면 (쉐어드 파일 자체가 없으면?)
                } else {
                    Log.d(TAG, "========================");
                    Log.d(TAG, "쉐어드 파일(위에서 File에 적은 바로 그 파일, 이름까지 똑같아야 함)이 존재하지 않음 ");

                    JSONObject userObject = new JSONObject();
                    Log.d(TAG, "========================");
                    Log.d(TAG, "userObject= " + userObject);
                    try {
                        // 첫번째 회원 정보를 JSONObject에 저장하기
                        userObject.put("name", nameString);
                        userObject.put("password", passwordString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "========================");
                    Log.d(TAG, "userObject= " + userObject);

                    JSONObject userJSONObject = new JSONObject();
                    try {
                        // 또 다른 JSONObject 만들어서 'userData'라는 키값에다가 위에서 만든 userObject를 value 로 넣음
                        userJSONObject.put("userData", userObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "========================");
                    Log.d(TAG, "userJSONObject= " + userJSONObject);

                    // 위의 userJSONObject가 {"userData":{"name":"111","
                    Iterator x = userJSONObject.keys();
                    JSONArray userArray = new JSONArray();
                    while (x.hasNext()) {
                        String key = (String) x.next();
                        try {
                            userArray.put(userJSONObject.get(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "========================");
                    Log.d(TAG, "userArray= " + userArray);

//                    JSONArray userJSONArray = new JSONArray();
//                    try {
//                        userJSONObject.toJSONArray(userJSONArray);
//                        Log.d(TAG, "========================");
//                        Log.d(TAG, "userJSONArray= " + userJSONArray);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    JSONObject objectFromArray = new JSONObject();
                    try {
                        objectFromArray.put("USER", userArray);
                        Log.d(TAG, "========================");
                        Log.d(TAG, "objectFromArray= " + objectFromArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String finalString = objectFromArray.toString();
                    Log.d(TAG, "========================");
                    Log.d(TAG, "finalString= " + finalString);

                    // 필요하지 않은 코드. 잘못된 코드!
//                    Iterator xx = objectFromArray.keys();
//                    JSONArray finalArray = new JSONArray();
//
//                    while (xx.hasNext()) {
//                        String key = (String) xx.next();
//                        try {
//                            finalArray.put(objectFromArray.get(key));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    Log.d(TAG, "========================");
//                    Log.d(TAG, "finalArray= " + finalArray);
//                    Log.d(TAG, "finalArray.toString()= " + finalArray.toString());

                    // 쉐어드에 저장하기
                    editor.putString("userKey", finalString);
                    editor.commit();
                    Log.d(TAG, "========================");
                    Log.d(TAG, "saved sharedpref value= " + sharedPreferences.getString("userKey", null));

                    nameEditText.setText("");
                    passwordEditText.setText("");
                }


//                if (savedSharedPrefString != null) {
//                    try {
//                        JSONObject loadedObject = new JSONObject(savedSharedPrefString);
//                        Log.d(TAG, "========================");
//                        Log.d(TAG, "loadedObject = " + loadedObject);
//                        loadedObject.put("name", nameString);
//                        loadedObject.put("password", passwordString);
//                        Log.d(TAG, "========================");
//                        Log.d(TAG, "loadedObject (after put) = " + loadedObject);
//
//                        JSONObject userJSONObject = new JSONObject();
//                        try {
//                            userJSONObject.put("userData", loadedObject);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Log.d(TAG, "========================");
//                        Log.d(TAG, "userJSONObject= " + userJSONObject);
//
//                        editor.putString("userKey", loadedObject.toString());
//                        editor.commit();
//                        Log.d(TAG, "========================");
//                        Log.d(TAG, "saved sharedpref value= " + sharedPreferences.getString("userKey", ""));
//
//                        nameEditText.setText("");
//                        passwordEditText.setText("");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                } else {
//                    JSONObject userObject = new JSONObject();
//                    Log.d(TAG, "========================");
//                    Log.d(TAG, "JSONObject= " + userObject);
//                    try {
//                        userObject.put("name", nameString);
//                        userObject.put("password", passwordString);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d(TAG, "========================");
//                    Log.d(TAG, "JSONObject= " + userObject);
//
//                    JSONObject userJSONObject = new JSONObject();
//                    try {
//                        userJSONObject.put("userData", userObject);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d(TAG, "========================");
//                    Log.d(TAG, "userJSONObject= " + userJSONObject);
//
//                    editor.putString("userKey", userJSONObject.toString());
//                    editor.commit();
//                    Log.d(TAG, "========================");
//                    Log.d(TAG, "saved sharedpref value= " + sharedPreferences.getString("userKey", ""));
//
//                    nameEditText.setText("");
//                    passwordEditText.setText("");
//                }



//                Iterator x = userJSONObject.keys();
//                JSONArray userArray = new JSONArray();
//
//                while (x.hasNext()) {
//                    String key = (String) x.next();
//                    try {
//                        userArray.put(userJSONObject.get(key));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.d(TAG, "========================");
//                Log.d(TAG, "userArray= " + userArray);
//
//                JSONObject objectFromArray = new JSONObject();
//                try {
//                    objectFromArray.put("USER", userArray);
//                    Log.d(TAG, "========================");
//                    Log.d(TAG, "objectFromArray= " + objectFromArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

//                String finalString = objectFromArray.toString();


//                Iterator xx = objectFromArray.keys();
//                JSONArray finalArray = new JSONArray();
//
//                while (xx.hasNext()) {
//                    String key = (String) xx.next();
//                    try {
//                        finalArray.put(objectFromArray.get(key));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.d(TAG, "========================");
//                Log.d(TAG, "finalArray= " + finalArray);
//                Log.d(TAG, "finalArray.toString()= " + finalArray.toString());


//                try {
//                    JSONArray userArray = userJSONObject.getJSONArray("userData");
//                    Log.d(TAG, "========================");
//                    Log.d(TAG, "userArray= " + userArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


//                sharedPreferences = getSharedPreferences("Users", MODE_PRIVATE);
//                editor = sharedPreferences.edit();


            }
        });
    }
}