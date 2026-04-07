package br.edu.ifsp.scl.pdm.pa2.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.pdm.pa2.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        amb.launchCoroutinesBt.setOnClickListener {
            val random = Random(System.currentTimeMillis())
            val SLEEP_LIMIT = 3000L


            //criando a corrotina
            GlobalScope.launch(Dispatchers.Default) {
                Log.v(
                    getString(R.string.app_name),
                    "Top Level coroutine thread: ${Thread.currentThread().name}, Job: ${coroutineContext[Job]}"
                )

                val upper = sleep("Upper", random.nextLong(SLEEP_LIMIT))
                withContext(Dispatchers.Main + Job()){
                    Log.v(
                        getString(R.string.app_name),
                        "But this code is executed in the main thread: ${Thread.currentThread().name}, Job: ${coroutineContext[Job]}"
                    )
                            amb.upperTv.text = upper
                    }
                Log.v(
                    getString(R.string.app_name),
                    "Top coroutine thread after main: ${Thread.currentThread().name}, Job: ${coroutineContext[Job]}"
                )


                launch(Dispatchers.IO){
                    Log.v(
                        getString(R.string.app_name),
                        "Lower Coroutine thread: ${Thread.currentThread().name}, Job: ${coroutineContext[Job]}"
                    )
                    sleep("Lower", random.nextLong(SLEEP_LIMIT)).let {
                        runOnUiThread {
                            amb.lowerTv.text = it
                        }
                    }
                    Log.v(getString(R.string.app_name), "Lower Coroutine completed")
                    }

                    Log.v(getString(R.string.app_name), "Top Coroutine completed")

                }

            Log.v(getString(R.string.app_name), "Main threed: ${Thread.currentThread().name}")
        }
    }
    private suspend fun sleep(name: String, time: Long): String {
        delay(time)
        return "$name slept for $time ms"
    }
}