package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.adapter.AuditAdapter
import com.example.techaudit20.databinding.ActivityMainBinding
import com.example.techaudit20.ui.AuditViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AuditAdapter

    private val viewmodel: AuditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        configurarDeslizarParaBorrar()

        viewmodel.allItems.observe(this) { listaActualizada ->
            adapter.actualizarLista(listaActualizada)
        }

        binding.fabAgregar.setOnClickListener {
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
        adapter = AuditAdapter(mutableListOf()) { itemSeleccionado ->
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_ITEM_EDITAR", itemSeleccionado)
            startActivity(intent)
        }

        binding.rvAuditoria.adapter = adapter
        binding.rvAuditoria.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarDeslizarParaBorrar() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val itemABorrar = adapter.listaAuditoria[posicion]

                viewmodel.delete(itemABorrar)
                Toast.makeText(this@MainActivity, "Equipo Eliminado", Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvAuditoria)
    }
}
