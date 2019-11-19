package org.techtown.kotlin_todolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_join.*
import org.techtown.kotlin_todolist.Room.User
import org.techtown.kotlin_todolist.Room.UserDB
import org.techtown.kotlin_todolist.Room.UserDao
import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.techtown.kotlin_todolist.Retrofit.Request.RegisterRequest
import org.techtown.kotlin_todolist.Retrofit.Response.RegisterUserResponse
import org.techtown.kotlin_todolist.RetrofitGenerator


class JoinActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        btn_join_ok.setOnClickListener {
            val email = join_email.text.toString()
            val pwd = join_pwd.text.toString()
            val pwdCheck = join_pwd2.text.toString()

            //signUp(email, pwd, pwdCheck);
            //Retrofit으로 회원 가입 연결
            val userRequest=RegisterRequest(email,pwd,pwdCheck)
            val call=RetrofitGenerator.create().registerUser(userRequest)

            call.enqueue(object : Callback<RegisterUserResponse> {
                override fun onResponse(call: Call<RegisterUserResponse>, response: Response<RegisterUserResponse>) {
                    Log.d("success", response.body()?.username.toString())
                    /*onFinishedListener.onFinished(1)*/

                    when(response!!.code()){
                        200->{
                            Toast.makeText(this@JoinActivity,"회원가입 성공",Toast.LENGTH_LONG).show()
                            finish()
                        }
                        405->Toast.makeText(this@JoinActivity,"회원가입 실패:아이디나 비반이 올바르지 않음",Toast.LENGTH_LONG).show()
                        500->Toast.makeText(this@JoinActivity,"서버 오류",Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                    Log.d("fail", "failed")
                    //onFinishedListener.onFailure(t)
                }
            })


            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    //Room으로 회원가입
    private fun signUp(email:String, pwd:String, pwdCheck:String){
        /*val userDb: UserDB? = UserDB.getInstance(this)
        val userDao:UserDao= userDb!!.userDao

        val userList = ArrayList<User>()
        val signUpThread = Thread { userList.addAll(userDao.findUser(email)) }
        signUpThread.start()

        try {
            signUpThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        if(userList.size !=0){
            return Toast.makeText(this,"이미 동일한 email이 존재합니다",Toast.LENGTH_LONG).show()
        }
        if(pwd != pwdCheck){
            return Toast.makeText(this,"비밀번호 확인은 비밀번호와 동일해야합니다.",Toast.LENGTH_LONG).show()
        }
        val user=User(email,pwd)
        Thread { userDb!!.userDao.insert(user) }.start()
        val intent=Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
         */
    }
}