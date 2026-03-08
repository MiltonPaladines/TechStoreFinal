package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.model.AuditItem
import com.example.techaudit20.adapter.AuditAdapter
import com.example.techaudit20.data.AuditDatabase
import com.example.techaudit20.databinding.ActivityMainBinding
import com.example.techaudit20.model.AuditItem
import com.example.techaudit.model.AuditStatus
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import kotlin.collections.mutableListOf

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AuditAdapter
    private lateinit var listaAuditoria: MutableList<AuditItem>

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = (application as TechAuditApp).database

        setupRecyclerView()

        cargarDatosdeBasedeDatos()


        binding.fabAgregar.setOnClickListener{
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
    }
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        //Inicializar el adapter pasando lista y la accion del clic

        adapter = AuditAdapter(listaAuditoria = mutableListOf()) { itemSeleccionado ->
            //Este lambda se ejecuta cuando doy clic a la tarjeta

            val intent = Intent(packageContext = this, cls = AddEditActivity::class.java)

            intent.putExtra(name = "EXTRA_ITEM_EDITAR", value = itemSeleccionado)

            startActivity(intent)

        }


        binding.rvAuditoria.adapter = adapter
        binding.rvAuditoria.layoutManager = LinearLayoutManager(this)
    }


    private fun configurarDeslizarParaBorrar() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0, // No nos importa mover arriba/abajo
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Permitir deslizar a izq y der
        ) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false

            // Este método se dispara cuando el usuario suelta el dedo tras deslizar
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val itemABorrar = adapter.listaAuditoria[posicion]

                lifecycleScope.launch {
                    // 1. Borrar de la Base de Datos
                    database.auditDao().delete(itemABorrar)

                    // 2. Borrar de la pantalla (Animación fluida)
                    adapter.listaAuditoria.removeAt(posicion)
                    adapter.notifyItemRemoved(posicion)

                    Toast.makeText(this@MainActivity, "Equipo Eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Conectamos este comportamiento a nuestra lista
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvAuditoria)
    }

    private fun cargarDatosdeBaseDeDatos() {

        lifecycleScope.launch {
            val datos = database.auditDao().getAllItems()

            if (datos.isEmpty()) {
                Toast.makeText(this@MainActivity, "No hay datos", Toast.LENGTH_SHORT).show()
            } else {
                adapter.actualizarLista(nuevaLista = datos)
            }
        }


        override fun onResume(){
        super.OnResume()
        cargarDatosdeBaseDeDatos()
        }

    }

}