package org.techtown.kotlin_todolist

import android.content.Context
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
    private val userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //자동 로그인
        val auto = getSharedPreferences("auto", Context.MODE_PRIVATE)
        val editor = auto.edit()
        val autoEmail=auto.getString("email","")
        val autoPwd=auto.getString("password","")

        //최근 아이디 저장
        val save = getSharedPreferences("save", Context.MODE_PRIVATE)
        val saveEditor = save.edit()
        val saveEmail=save.getString("email","")

        login_email.setText(saveEmail)

        //자동로그인 시 event
        if (autoEmail != null && autoPwd != null) {
            if(checkLogin(autoEmail,autoPwd)) {
                Toast.makeText(this, "자동 로그인 되었습니다.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        btn_login.setOnClickListener {
            val email = login_email.text.toString()
            val pwd = login_pwd.text.toString()
            when {
                checkLogin(email, pwd) -> {
                    Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_LONG).show()
                    //자동 로그인 체크시
                    if (autoLogin.isChecked){
                        editor.putString("email", email)
                        editor.putString("password", pwd)
                        editor.commit()
                    }
                    saveEditor.putString("email",email)
                    saveEditor.commit()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                checkId(email) -> Toast.makeText(
                    this,
                    "비밀번호 정보가 잘못되었습니다.",
                    Toast.LENGTH_LONG
                ).show()
                else -> Toast.makeText(this, "로그인 정보가 잘못되었습니다.", Toast.LENGTH_LONG).show()
            }
        }

            btn_join.setOnClickListener {
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
                finish()
            }
    }

    //아이디와 비밀번호 체크
    private fun checkLogin(email: String, pwd: String): Boolean {
        val userDb: UserDB? = UserDB.getInstance(this)
        val userDao: UserDao = userDb!!.userDao
        //db, 서버 접근 시에는 쓰레드를 따로 해야함.
        val loginThread = Thread { userList.addAll(userDao.userLogin(email, pwd)) }
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
    private fun checkId(email: String): Boolean {
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