package com.example.techaudit20


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding

    private var itemEditar: AuditItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

//Detectar MODO EDICION
        if (intent.hasExtra(name = "EXTRA_ITEM_EDITAR")) {
            //Recuperamos el objeto
            itemEditar = if (android.os.Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(name = "EXTRA_ITEM_EDITAR", clazz = AuditItem::class.java)
            }else {
                @Suppress(names = "DEPRECATION")
                intent.getParcelableExtra(name = "EXTRA_ITEM_EDITAR")
            }

            itemEditar?.let { item ->
                binding.etNombre.setText(item.nombre)
                binding.etUbicacion.setText(item.ubicacion)
                binding.etNotas.setText(item.notas)

                //Seleccionar el Spinner Correcto
                val posicionSpinner = AuditStatus.values().indexOf(item.estado)
                binding.spEstado.setSelection(posicionSpinner)

            }
        }



        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSpinner()

        binding.btnGuardar.setOnClickListener {
            guardarOActualizar()
        }
    }

    private fun setupSpinner() {
        val estados = AuditStatus.values()
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            estados
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstado.adapter = adapter
    }

    private fun guardarOActualizar() {
        val nombre = binding.etNombre.text.toString()
        val ubicacion = binding.etUbicacion.text.toString()
        val notas = binding.etNotas.text.toString()

        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y Ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val estadoSeleccionado = binding.spEstado.selectedItem as AuditStatus

        val database = (application as TechAuditApp).database

        lifecycleScope.launch {
            if (itemEditar == null) {
                //CREAR
                val nuevoItem = AuditItem(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    ubicacion = ubicacion,
                    fechaRegistro = Date().toString(),
                    estado = estadoSeleccionado,
                    notas = notas,
                )

                database.auditDao().insert(nuevoItem)

                Toast.makeText(context = this@AddEditActivity, text = "Equipo Creado", duration = Toast.LENGTH_SHORT).show()
            } else {
                //EDITAR
                val itemActualizado = itemEditar!!.copy(
                    nombre = nombre,
                    ubicacion = ubicacion,
                    estado = estadoSeleccionado,
                    notas = notas,
                )

                database.auditDao().update(itemActualizado)
                Toast.makeText(context = this@AddEditActivity, text = "Equipo Actualizado", duration = Toast.LENGTH_SHORT).show()
            }

            finish() //Regresar al Main
        }
    }
}