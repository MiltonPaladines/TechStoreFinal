package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.techaudit20.adapter.LabAdapter
import com.example.techaudit20.databinding.ActivityLabBinding
import com.example.techaudit20.model.Laboratorio
import com.example.techaudit20.ui.AuditViewModel
import java.util.UUID

class LabActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLabBinding
    private val viewModel: AuditViewModel by viewModels()
    private lateinit var adapter: LabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Observar laboratorios
        viewModel.allLaboratorios.observe(this) { labs ->
            adapter.updateList(labs)
        }


        binding.fabAddLab.setOnClickListener {
            showAddLabDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = LabAdapter(emptyList()) { lab ->

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("LAB_ID", lab.id)
            intent.putExtra("LAB_NOMBRE", lab.nombre)
            startActivity(intent)
        }
        binding.rvLabs.adapter = adapter
    }

    private fun showAddLabDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nuevo Laboratorio")


        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 10)

        val inputNombre = EditText(this).apply { hint = "Nombre (Ej: Redes)" }
        val inputEdificio = EditText(this).apply { hint = "Edificio (Ej: Bloque B)" }

        layout.addView(inputNombre)
        layout.addView(inputEdificio)
        builder.setView(layout)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nombre = inputNombre.text.toString()
            val edificio = inputEdificio.text.toString()
            if (nombre.isNotBlank() && edificio.isNotBlank()) {
                val newLab = Laboratorio(UUID.randomUUID().toString(), nombre, edificio)
                viewModel.insertLaboratorio(newLab)
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}