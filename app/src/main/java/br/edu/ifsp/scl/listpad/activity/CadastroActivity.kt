package br.edu.ifsp.scl.listpad.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.databinding.ActivityCadastroBinding
import br.edu.ifsp.scl.listpad.model.ShoppingList
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CadastroActivity : AppCompatActivity() {
    private var activityCadastroBinding: ActivityCadastroBinding? = null
    var cal = Calendar.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCadastroBinding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_cadastro)

        val btnNext = findViewById<ExtendedFloatingActionButton>(R.id.btNext)

        findViewById<ExtendedFloatingActionButton>(R.id.btBack).setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ExtendedFloatingActionButton>(R.id.btNext).setOnClickListener {
            observerFields(findViewById(R.id.etData), btnNext)
        }
    }

    fun imageButtonOnClick(view: View) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        val datePicker = DatePickerDialog(
            this@CadastroActivity,
            android.R.style.Theme_Material_Dialog_MinWidth,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()

        print(dateSetListener)
    }

    private fun observerFields(field: EditText, button: ExtendedFloatingActionButton){
        if (field.text.isNotEmpty()) {
            partialSave()
            val intent = Intent(applicationContext, CadastroItemsActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this@CadastroActivity, "Por favor! Para prosseguir preencha todos os campos", Toast.LENGTH_LONG).show()
        }
    }

    private fun partialSave() {
        val nome = findViewById<EditText>(R.id.etNome).text.toString()
        val descricao = findViewById<EditText>(R.id.etDescricao).text.toString()
        val data = findViewById<EditText>(R.id.etData).text.toString()

        val list = ShoppingList( nome, descricao, data)
        db.collection("shoppinglist").add(list)
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val dateString = sdf.format(cal.time)
        findViewById<EditText>(R.id.etData).setText(dateString)
    }
}
