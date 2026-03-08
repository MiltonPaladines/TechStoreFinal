package com.example.techaudit20

import android.app.Application
import com.example.techaudit20.data.AuditDatabase

class TechAuditApp: Application() {

 val database by lazy { AuditDatabase.getDatabase(context = this) }
}