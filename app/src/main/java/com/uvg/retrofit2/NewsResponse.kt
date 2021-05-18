package com.uvg.retrofit2

import com.google.gson.annotations.SerializedName

data class NewsResponse (
    @SerializedName("status") val status:String,
    @SerializedName("totalResults") val totalStatus:String,
    @SerializedName("articles") val articles:List<Articles>
)