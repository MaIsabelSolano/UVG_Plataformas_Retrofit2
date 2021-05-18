package com.uvg.retrofit2

import android.annotation.SuppressLint
import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.uvg.retrofit2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticleAdapter
    private val articleList = mutableListOf<Articles>()
    private lateinit var pais:String
    private lateinit var tipo:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchNews.setOnQueryTextListener(this)

        //default
        pais = "us"
        tipo = "general"

        initRecyclerView()
        searchNew(pais,tipo)

        //cambio de pais
        binding.paisUSA.setOnClickListener {
            pais = "us"
            searchNew(pais,tipo)

        }
        binding.paisJP.setOnClickListener {
            pais = "jp"
            searchNew(pais,tipo)
        }
        binding.paisMX.setOnClickListener {
            pais = "mx"
            searchNew(pais,tipo)
        }
    }

    private fun initRecyclerView(){

        adapter = ArticleAdapter(articleList)
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.adapter = adapter
    }

    private fun searchNew(country:String,category:String){

        val api = Retrofit2()

        CoroutineScope(Dispatchers.IO).launch {

            val call = api.getService()?.getNewsByCategory(country,category,"a47b8482142b4f76a6101f9649ab28ba")
            val news: NewsResponse? = call?.body()

            runOnUiThread{
                if (call!!.isSuccessful){
                    if (news?.status.equals("ok")){
                        val articles= news?.articles ?: emptyList()
                        articleList.clear()
                        articleList.addAll(articles)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        showMessage("Error en webservice")
                    }
                }else{
                    showMessage("Error en retrofit")
                }
                hideKeyBoard()
            }
        }
    }

    private fun showMessage(mensaje:String){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchNew(pais,query.toLowerCase(Locale.ROOT))
            tipo = query.toLowerCase(Locale.ROOT)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }


}