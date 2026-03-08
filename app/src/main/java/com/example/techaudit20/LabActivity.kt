package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.techaudit20.adapter.LabAdapter
import com.example.techaudit20.databinding.ActivityLabBinding
import com.example.techaudit20.model.Laboratorio
import com.example.techaudit20.network.RetrofitClient
import com.example.techaudit20.ui.AuditViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        viewModel.allLaboratorios.observe(this) { labs ->
            adapter.updateList(labs)
        }

        binding.fabAddLab.setOnClickListener {
            showAddLabDialog()
        }


        binding.btnSincronizar.setOnClickListener {
            sincronizarDatos()
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


        val inputId = EditText(this).apply { hint = "ID del Laboratorio (Ej: 1)" }
        val inputNombre = EditText(this).apply { hint = "Nombre (Ej: Redes)" }
        val inputEdificio = EditText(this).apply { hint = "Edificio (Ej: Bloque B)" }

        layout.addView(inputId)
        layout.addView(inputNombre)
        layout.addView(inputEdificio)
        builder.setView(layout)

        builder.setPositiveButton("Guardar") { _, _ ->
            val id = inputId.text.toString().trim()
            val nombre = inputNombre.text.toString().trim()
            val edificio = inputEdificio.text.toString().trim()

            if (id.isNotBlank() && nombre.isNotBlank() && edificio.isNotBlank()) {

                val newLab = Laboratorio(id, nombre, edificio)
                viewModel.insertLaboratorio(newLab)
            } else {
                Toast.makeText(this, "Complete todos los campos, incluido el ID", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun sincronizarDatos() {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val dao = (application as TechAuditApp).database.auditDao()
                val api = RetrofitClient.instance

                withContext(Dispatchers.IO) {

                    val todosLosEquipos = dao.getAllEquiposStatic()
                    val labsLocales = dao.getAllLaboratoriosStatic()


                    labsLocales.forEach { lab ->

                        val respuestaPut = api.actualizarLaboratorio(lab.id, lab)

                        if (!respuestaPut.isSuccessful && respuestaPut.code() == 404) {

                            api.crearLaboratorio(lab)
                        }
                    }


                    val pendientesBorrar = todosLosEquipos.filter { it.eliminado }
                    pendientesBorrar.forEach { equipo ->
                        val resDel = api.eliminarEquipo(equipo.id)
                        if (resDel.isSuccessful || resDel.code() == 404) {
                            dao.borradoFisico(equipo)
                        }
                    }


                    val paraSincronizar = todosLosEquipos.filter { !it.eliminado }
                    paraSincronizar.forEach { equipo ->
                        val respuestaPut = api.actualizarEquipo(equipo.id, equipo)
                        if (!respuestaPut.isSuccessful && respuestaPut.code() == 404) {
                            api.crearEquipo(equipo)
                        }
                    }


                    val resLabs = api.obtenerLaboratorios()
                    val resEquipos = api.obtenerEquipos()

                    if (resLabs.isSuccessful && resEquipos.isSuccessful) {
                        val labsNube = resLabs.body() ?: emptyList()
                        val equiposNube = resEquipos.body() ?: emptyList()


                        labsNube.forEach { dao.insertLaboratorio(it) }


                        equiposNube.forEach { equipo ->
                            if (labsNube.any { it.id == equipo.laboratorioId }) {
                                dao.insert(equipo)
                            }
                        }
                    }
                }

                Toast.makeText(this@LabActivity, "Sincronización Total Exitosa", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(this@LabActivity, "Error de red: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}