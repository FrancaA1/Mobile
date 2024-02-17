package com.senai.vsconnect_kotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.JsonObject
import com.senai.vsconnect_kotlin.apis.EndpointInterface
import com.senai.vsconnect_kotlin.apis.RetrofitConfig
import com.senai.vsconnect_kotlin.databinding.ActivityLoginBinding
import com.senai.vsconnect_kotlin.models.Login
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

class LoginActivity : AppCompatActivity() {

    //É uma propriedade privada  como o nome binding do tipo ActivityLoginBinding
    private lateinit var binding: ActivityLoginBinding

    private val clienteRetrofit = RetrofitConfig.obterInstanciaRetrofit()

    private val endpoints = clienteRetrofit.create(EndpointInterface::class.java)

    private val IMAGEM_PERFIL_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Atribui à variável binding um objeto que contém referências (propriedades) aos elementos definidos no layout
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val sharedPreferences = getSharedPreferences("idUsuario", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.remove("idUsuario")

        editor.apply()

        //setOnClickListener é um ouvinte de clique
        //Ou seja, quando clicar no botão entrar irá cair nesse bloco
        binding.btnEntrar.setOnClickListener {
//            autenticarUsuario()
//            val mainIntent = Intent(this@LoginActivity, MainActivity::class.java  )
//
//            startActivity(mainIntent)
            mostrarOpcoesEscolhaImagem()
        }

        setContentView(binding.root)

    }

    private fun mostrarOpcoesEscolhaImagem(){
        // Criar Intents para escolher uma imagem da galeria ou capturar uma nova pela câmera
//        val escolherImagemIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val capturarImagemIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Obter os títulos para as opções de escolha de imagem
//        val escolherImagemTitle = resources.getString(R.string.escolher_imagem)
        val capturarImagemTitle = resources.getString(R.string.capturar_imagem)

        // Criar um Intent Chooser para oferecer opções entre galeria e câmera
//        val intentEscolhaImagem = Intent.createChooser(escolherImagemIntent, escolherImagemTitle)
        val intentEscolhaImagem = Intent.createChooser(capturarImagemIntent, capturarImagemTitle)
        intentEscolhaImagem.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(capturarImagemIntent))

        // Iniciar a atividade esperando um resultado
        startActivityForResult(intentEscolhaImagem, IMAGEM_PERFIL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        val view_imagem_perfil = binding.

        if (requestCode == IMAGEM_PERFIL_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data?.action == "inline-data"){
                // Imagem capturada pela câmera
                val imagemCapturada = data.extras?.get("data") as Bitmap

                atualizarImagemPerfil(imagemCapturada)
            }
        }
    }

    private fun atualizarImagemPerfil(imagem: Bitmap){

        // Criar um arquivo temporário para armazenar a imagem
        val file = File(cacheDir, "temp_image.png")
        file.createNewFile()

        // Salvar a imagem Bitmap no arquivo temporário
        val outputStream = FileOutputStream(file)
        imagem.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        // Criar partes Multipart para a imagem
        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val imagemPart = MultipartBody.Part.createFormData("image", file.name, requestFile)
1
        endpoints.login(imagemPart).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                when( response.code() ){
                    200 -> {
                        ,111111111111111111111111n nnjq11111q// Exibir mensagem de sucesso
                        Toast.makeText(this@LoginActivity, "deu bom", Toast.LENGTH_SHORT).show()

                    }
                    else -> {
                        Toast.makeText(this@LoginActivity, "mamamo", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Exibir mensagem de erro
                Toast.makeText(this@LoginActivity, "Erro ao atualizar imagem de Perfil.", Toast.LENGTH_SHORT).show()
            }

        })
    }

}
