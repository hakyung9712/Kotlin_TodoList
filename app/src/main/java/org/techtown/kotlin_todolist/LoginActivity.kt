package org.techtown.kotlin_todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.techtown.kotlin_todolist.Room.User
import org.techtown.kotlin_todolist.Room.UserDB
import org.techtown.kotlin_todolist.Room.UserDao
import java.util.ArrayList

class LoginActivity: AppCompatActivity() {
    val userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            val email=login_email.text.toString()
            val pwd=login_pwd.text.toString()
            if(checkLogin(email,pwd)){
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else if(checkId(email)){
                Toast.makeText(this,"비밀번호 정보가 잘못되었습니다.",Toast.LENGTH_LONG).show()
            }
            else{
               Toast.makeText(this,"로그인 정보가 잘못되었습니다.",Toast.LENGTH_LONG).show()
            }
        }

        btn_join.setOnClickListener {
            val intent=Intent(this,JoinActivity::class.java)
            startActivity(intent)
        }
    }

    //아이디와 비밀번호 체크
    fun checkLogin(email:String, pwd:String):Boolean{
        val userDb: UserDB? = UserDB.getInstance(this)
        val userDao: UserDao = userDb!!.userDao
        val loginThread = Thread { userList.addAll(userDao.userLogin(email,pwd)) }
        loginThread.start()

        try {
            loginThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return if (userList.size == 0) {
            false
        } else userList[0].email == email

    }

    //아이디는 맞으나 비밀번호가 틀린경우
    fun checkId(email:String):Boolean{
        val userDb: UserDB? = UserDB.getInstance(this)
        val userDao: UserDao = userDb!!.userDao
        val checkIdThread = Thread { userList.addAll(userDao.findUser(email)) }
        checkIdThread.start()

        try {
            checkIdThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return if (userList.size == 0) {
            false
        } else userList[0].email == email
    }
}