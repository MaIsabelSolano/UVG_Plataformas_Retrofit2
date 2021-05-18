package com.uvg.retrofit2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.uvg.retrofit2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticleAdapter
    private val articleList = mutableListOf<Articles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }

    private fun initRecyclerView(){

        adapter = ArticleAdapter(articleList)
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.adapter = adapter
    }

    private fun searchNew(category:String){

        val api = Retrofit2()

        CoroutineScope(Dispatchers.IO).launch {

            val call = api.getService()?.getNewsByCategory("us",category,"4b94054dbc6b4b3b9e50d8f62cde4f6c")
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
            }
        }
    }

    private fun showMessage(mensaje:String){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}