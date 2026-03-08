package com.example.techaudit20

import android.app.Application
import com.example.techaudit20.data.AuditDatabase

class TechAuditApp: Application() {

    //Lazy: La base de datos solo se crea cuando alguien la pide por primera vez
    val database by lazy { AuditDatabase.getDatabase(context = this) }
}