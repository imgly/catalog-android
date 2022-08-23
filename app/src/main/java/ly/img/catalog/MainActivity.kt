package ly.img.catalog

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ly.img.catalog.examples.Example
import ly.img.catalog.examples.photoExamples
import ly.img.catalog.examples.videoExamples

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    private var showPESDKExamples = true

    private var currentExample: Example? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = CatalogAdapter {
            currentExample = it.example.java.getConstructor(AppCompatActivity::class.java)
                .newInstance(this)
                .also { example ->
                    example.invoke()
                }
        }
        setFilteredList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentExample?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(if (showPESDKExamples) R.id.show_pesdk else R.id.show_vesdk).isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked
        when (item.itemId) {
            R.id.show_pesdk -> showPESDKExamples = item.isChecked
            R.id.show_vesdk -> showPESDKExamples = !item.isChecked
        }
        setFilteredList()
        return true
    }

    private fun setFilteredList() {
        (recyclerView.adapter as CatalogAdapter).setList(if (showPESDKExamples) photoExamples else videoExamples)
    }
}