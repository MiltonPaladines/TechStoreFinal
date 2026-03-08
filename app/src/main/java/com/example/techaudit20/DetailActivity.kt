package com.example.techaudit20

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit20.databinding.ActivityDetailBinding
import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.AuditStatus

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializamos el binding y establecemos la vista una sola vez
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manejo de Insets para el diseño Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Recuperamos el objeto enviado
        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_ITEM", AuditItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<AuditItem>("EXTRA_ITEM")
        }

        // 2. Mostrar datos si el objeto existe
        item?.let {
            mostrarDetalles(it)
        }
    }

    private fun mostrarDetalles(item: AuditItem) {
        binding.tvDetalleNombre.text = item.nombre
        binding.tvDetalleId.text = "ID: ${item.id.take(8)}..." // Mostrar solo 8 caracteres
        binding.tvDetalleUbicacion.text = item.ubicacion
        binding.tvDetalleFecha.text = item.fechaRegistro
        binding.tvDetalleNotas.text = item.notas.ifEmpty { "Sin notas registradas." }

        // 3. Lógica visual según el estado (Pintar la cabecera)
        val color = when (item.estado) {
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.PENDIENTE -> Color.parseColor("#FFC107")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK
        }

        binding.viewHeaderStatus.setBackgroundColor(color)
        title = "Detalle: ${item.estado.name}"
    }
}
