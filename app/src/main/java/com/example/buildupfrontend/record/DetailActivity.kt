package com.example.buildupfrontend.record

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityDetailBinding
import com.example.buildupfrontend.retrofit.Client.ActivityService
import com.example.buildupfrontend.retrofit.Response.ActivityDetail
import com.example.buildupfrontend.retrofit.Response.ActivityDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var activityId:Long=0
    private lateinit var detailData: ActivityDetail
    private lateinit var imageString:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarActivityDetail)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        activityId=intent.getLongExtra("activityId",0)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.ivDetail.setOnClickListener {
            var intent= Intent(this, ShowImagesActivity::class.java)
            intent.putStringArrayListExtra("imageList",imageString)
            intent.putExtra("page",0)
            startActivity(intent)
        }

//        Glide.get(this).clearDiskCache();
//        Glide.get(this).clearMemory();
    }

    override fun onResume() {
        super.onResume()
        imageString= arrayListOf()

        showLoading(true)
        loadDetail()
    }

    private fun loadDetail(){
        ActivityService.retrofitActivityDetail(activityId).enqueue(object :
            Callback<ActivityDetailResponse> {
            override fun onResponse(
                call: Call<ActivityDetailResponse>,
                response: Response<ActivityDetailResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    var data=response.body()?.response
                    detailData=data!!

                    binding.tvTitleDetail.text=data?.activityName
                    binding.tvCategoryDetail.text=data?.categoryName
                    binding.tvDate.text="${data?.startDate} ~ ${data?.endDate}"

                    if(data?.hostName=="")
                        binding.linearHost.visibility=View.GONE
                    else {
                        binding.linearHost.visibility=View.VISIBLE
                        binding.tvHost.text = data?.hostName
                    }

                    if(data?.roleName=="")
                        binding.linearRole.visibility=View.GONE
                    else {
                        binding.linearRole.visibility=View.VISIBLE
                        binding.tvRole.text = data?.roleName
                    }

                    if(data?.urlName=="")
                        binding.linearUrl.visibility=View.GONE
                    else {
                        binding.linearUrl.visibility=View.VISIBLE
                        binding.tvUrl.text = data?.urlName
                    }

                    if(data?.activityimg==null)
                        binding.ivDetail.visibility=View.GONE
                    else {
                        imageString.add(data?.activityimg)

                        Glide.with(this@DetailActivity)
                            .load(imageString[0])
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .fitCenter()
                            .error(R.drawable.ic_add_image) // 로딩 에러 발생 시 표시할 이미지
                            .apply(RequestOptions().override(500, 500))
                            .into(binding.ivDetail)
                        
                        showLoading(false)
                    }

                }else {
                    try {
                        val body = response.errorBody()!!.string()

                        Log.e(ContentValues.TAG, "body : $body")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ActivityDetailResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
        })
    }

    private fun showLoading(isShow: Boolean){
        if(isShow==true) {
            binding.progressDetail.visibility = View.VISIBLE
        }
        else {
            binding.progressDetail.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.activity_detail_menu,
            menu
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_activity -> {
                var intent= Intent(this,EditActivityActivity::class.java)

                intent.putExtra("activityId",detailData.activityId)
                intent.putExtra("categoryName",detailData.categoryName)
                intent.putExtra("activityName",detailData.activityName)
                intent.putExtra("hostName",detailData.hostName)
                intent.putExtra("roleName",detailData.roleName)
                intent.putExtra("startDate",detailData.startDate)
                intent.putExtra("endDate",detailData.endDate)
                intent.putExtra("urlName",detailData.urlName)
                intent.putExtra("activityimg", detailData.activityimg)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}