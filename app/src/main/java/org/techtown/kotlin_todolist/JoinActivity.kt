package org.techtown.kotlin_todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_join.*
import org.techtown.kotlin_todolist.Room.User
import org.techtown.kotlin_todolist.Room.UserDB
import org.techtown.kotlin_todolist.Room.UserDao
import java.util.ArrayList

class JoinActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        btn_join_ok.setOnClickListener {
            val email = join_email.text.toString()
            val pwd = join_pwd.text.toString()
            val pwdCheck = join_pwd2.text.toString()

            signUp(email, pwd, pwdCheck);
        }
    }

    private fun signUp(email:String, pwd:String, pwdCheck:String){
        val userDb: UserDB? = UserDB.getInstance(this)
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
    }
}